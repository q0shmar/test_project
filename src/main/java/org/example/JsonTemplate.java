package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.PathNotFoundException;
import org.json.JSONObject;

public class JsonTemplate {
    private final JSONObject template;
    private final ObjectMapper mapper;

    public JsonTemplate(JSONObject template) {
        this.template = template;
        this.mapper = new ObjectMapper();
    }

    public JsonTemplate set(String path, Object value) {
        if (value != null) {
            try {
                JsonNode node = mapper.readTree(template.toString());
                ((ObjectNode) node).set(path, mapper.valueToTree(value));
                template.put("", node);
            } catch (JsonProcessingException e) {
                throw new PathNotFoundException("Не удалось установить значение по пути: %s".formatted(path), e);
            }
        }
        return this;
    }

    public JsonTemplate set(String s, Object o, boolean delete) {
        if (delete) o = null;
        return setIfNullRemove(s, o);
    }

    public JsonTemplate put(String path, String key, Object value) {
        if (value != null) {
            try {
                JsonNode node = mapper.readTree(template.toString());
                ((ObjectNode) node).set(path, mapper.valueToTree(value));
                template.put(key, node);
            } catch (JsonProcessingException e) {
                throw new PathNotFoundException("Не удалось добавить значение по пути: %s".formatted(path), e);
            }
        }
        return this;
    }

    public JsonTemplate add(String path, Object value) {
        if (value != null) {
            try {
                JsonNode node = mapper.readTree(template.toString());
                ((ObjectNode) node).putArray(path).add(mapper.valueToTree(value));
                template.put("", node);
            } catch (JsonProcessingException e) {
                throw new PathNotFoundException("Не удалось добавить значение по пути: %s".formatted(path), e);
            }
        }
        return this;
    }

    public JsonTemplate setIfNullRemove(String path, Object value) {
        try {
            if (value == null) {
                JsonNode node = mapper.readTree(template.toString());
                ((ObjectNode) node).remove(path);
                template.put("", node);
            } else {
                set(path, value);
            }
        } catch (JsonProcessingException e) {
            throw new PathNotFoundException("Не удалось установить или удалить значение по пути: %s".formatted(path), e);
        }
        return this;
    }

    public JsonTemplate remove(String path, boolean condition) {
        if (condition) {
            try {
                JsonNode node = mapper.readTree(template.toString());
                ((ObjectNode) node).remove(path);
                template.put("", node);
            } catch (JsonProcessingException e) {
                throw new PathNotFoundException("Не удалось удалить значение по пути: %s".formatted(path), e);
            }
        }
        return this;
    }

    public JsonTemplate remove(String path) {
        try {
            JsonNode node = mapper.readTree(template.toString());
            ((ObjectNode) node).remove(path);
            template.put("", node);
        } catch (JsonProcessingException e) {
            throw new PathNotFoundException("Не удалось удалить значение по пути: %s".formatted(path), e);
        }
        return this;
    }

    public JSONObject build() {
        return template;
    }
}
