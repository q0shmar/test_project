package org.example;

import io.restassured.internal.util.IOUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Base64;

@Log4j2
@UtilityClass
public class AESDecryptor {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS7Padding";
    private static final Logger log = LoggerFactory.getLogger(AESDecryptor.class);
    private static boolean initialized = false;

    private static SecretKey generateKey(String password) {
        // Используем SHA-256 для получения ключа длиной 256 бит
        var keyBytes = new byte[32];
        System.arraycopy(password.getBytes(StandardCharsets.UTF_8), 0, keyBytes, 0, Math.min(password.length(), keyBytes.length));
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public static String decryptFile(String password, String resourcePath) {
        initialize();
        Cipher cipher;

        try {
            cipher = Cipher.getInstance(TRANSFORMATION, "BC");
            cipher.init(Cipher.DECRYPT_MODE, generateKey(password));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | NoSuchProviderException e) {
            throw new RuntimeException("Ошибка инициализации шифрования: %s".formatted(e.getMessage()), e);
        }

        var decryptedText = "";
        try (var is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("Ресурс не найден: %s".formatted(resourcePath));
            }

            log.info("Загрузка данных с ресурса \"{}\"", resourcePath);
            var encodedBytes = IOUtils.toByteArray(is);
            var decodedBytes = Base64.getDecoder().decode(encodedBytes);
            var outputBytes = cipher.doFinal(decodedBytes);
            decryptedText = new String(outputBytes, StandardCharsets.UTF_8);
        } catch (BadPaddingException | IllegalBlockSizeException | IOException e) {
            throw new RuntimeException(e);
        }
        return decryptedText;
    }

    private static void initialize() {
        if (initialized) return;
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }
}
