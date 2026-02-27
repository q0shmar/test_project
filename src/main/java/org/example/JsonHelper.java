package org.example;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@UtilityClass
@Log4j2
public class JsonHelper {
    private static final ObjectMapper mapper = JsonMapper.builder()
            .propertyNamingStrategy(PropertyNamingStrategies.SnakeCaseStrategy.INSTANCE)
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .addModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();
    private static final Logger log = LoggerFactory.getLogger(JsonHelper.class);

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Error converting object to JSON: {}", obj, e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Error deserializing JSON to object: {}", json, e);
            throw new RuntimeException(e);
        }
    }

    public static JSONArray getJsonStringFromObj(Object object) {
        try {
            return new JSONArray(toJson(object));
        } catch (Exception e) {
            log.error("Error converting object to JSON array: {}", object, e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T convertResponseOnClass(String rawJson, Class<T> clazz) {
        try {
            var jsonObject = new JSONObject(rawJson);
            return mapper.convertValue(jsonObject.toMap(), clazz);
        } catch (Exception e) {
            log.error("Error converting JSON to object: {}", rawJson, e);
            throw new RuntimeException(e);
        }
    }

    public static String getReqJson(String jsonName) {
        log.info("Forming request body '{}'", jsonName);
        var serviceName = System.getProperty("serviceName");
        var reqJsonPath = serviceName.replace("-", "_");
        return getStringFromJsonFile(String.format(reqJsonPath.concat("%s.json"), jsonName));
    }

    public static String getStringFromJsonFile(String path) {

        return FileLoaderHelper.loadStringFromFile("json%s".formatted(path));
    }

    public static JSONObject getJSONObjectFromFile(String file) {
        try {
            return new JSONObject(getStringFromJsonFile(file));
        } catch (Exception ex) {
            log.error("Error loading JSON from file: {}", file, ex);
            throw new RuntimeException(ex);
        }
    }

    public static String stringPrettyFormat(String text) {
        try {
            text = new JSONObject(text).toString(4);
        } catch (JSONException ex) {
            try {
                text = new JSONArray(text).toString(4);
            } catch (JSONException ex1) {
                return text;
            }
        }
        return text;
    }

    public static JsonTemplate getJsonTemplate(String file) {
        return new JsonTemplate(getJSONObjectFromFile(file));
    }

    public static <T> T getValueFromJson(String json, String pathToKey) {
        try {
            return JsonPath.parse(json, Configuration.defaultConfiguration()).read(pathToKey);
        } catch (Exception e) {
            log.error("Error getting value from JSON: {}", json, e);
            throw new RuntimeException(e);
        }
    }
}
