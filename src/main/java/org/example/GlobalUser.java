package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.Role;

import java.util.Arrays;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlobalUser {
    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("email")
    private String email;

    @JsonProperty("role")
    private Role role;

    public GlobalUser createObject() {
        var env = System.getProperty("env", "ift07");
        var secretPass = Reader.getProperty("secret");
        var dataFolder = Reader.getProperty("data.folder");
//        var decryptedFile = AESDecryptor.decryptFile(secretPass, "test_data/%s/secret.bin".formatted(env));
        var decryptedFile = AESDecryptor.decryptFile(secretPass, dataFolder.concat("/secret.bin"));
        var deserialize = JsonHelper.deserialize(decryptedFile, GlobalUser[].class);
        return Arrays.stream(deserialize)
                .filter(user -> user.getRole().equals(role))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Пользователь с ролью %s не найден".formatted(role)));
    }
}
