package org.example;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@UtilityClass
@Log4j2
public class Reader {
    private static final Properties PROPERTIES = new Properties();
    private static final Logger log = LoggerFactory.getLogger(Reader.class);

    static {
        try {
            var env = System.getProperty("env", "ift05");
            var envPropertiesStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/%s.properties".formatted(env));
            if (envPropertiesStream == null) {
                throw new IllegalStateException("%s.properties не найден в classpath".formatted(env));
            }
            PROPERTIES.load(new InputStreamReader(envPropertiesStream, StandardCharsets.UTF_8));
            log.debug("{}.properties загружен успешно.", env);

        } catch (IOException e) {
            throw new IllegalStateException("Couldn't read property files!", e);
        }

        // Замена значений свойств, если они установлены в системе
        for (String key : PROPERTIES.stringPropertyNames()) {
            var property = System.getProperty(key);
//            var property = PROPERTIES.getProperty(key);
            if (property != null) {
                PROPERTIES.setProperty(key, property);
            }
        }
    }

    public static Properties getProperties() {
        return PROPERTIES;
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static String getAppProp(String propertyKey, String defaultValue) {
        return PROPERTIES.getProperty(propertyKey, defaultValue);
    }

    public static Map<String, String> getAppPropStartWidth(String propertyKey) {
        return PROPERTIES.stringPropertyNames().stream()
                .filter(p -> p.startsWith(propertyKey))
                .collect(Collectors.toMap(p -> p, Reader::getProperty));
    }

    public static void setProperty(String key, String value) {
        PROPERTIES.setProperty(key, value);
    }
}
