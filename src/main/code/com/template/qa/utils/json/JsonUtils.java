package com.template.qa.utils.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.github.jknack.handlebars.internal.lang3.StringUtils.isNotBlank;
import static com.github.wnameless.json.flattener.JsonFlattener.flatten;
import static com.template.qa.utils.StringUtils.processNullValue;
import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBefore;

@Slf4j
public class JsonUtils {

    private JsonUtils() {
    }

    public static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

    @SneakyThrows
    public static String toJson(Object object) {
        return MAPPER.writeValueAsString(object);
    }

    @SneakyThrows
    public static Map<String, Object> toFlatMap(Object object) {
        return toMap(flatten(toJson(object)));
    }

    @SuppressWarnings("unchecked")
    public static <T, R> Map<T, R> toMap(String json) {
        return toObject(json, Map.class);
    }

    public static <T> T updateObjectStructure(final Object initObject,
                                              final List<JsonModifier> modifiers, final Class<T> model) {
        DocumentContext document = JsonPath.parse(toJson(initObject));

        modifiers.forEach(el -> {
            switch (el.operation()) {
                case DELETE -> document.delete(el.field().path());
                case UPDATE -> document.set(el.field().path(), el.field().value());
                case INSERT -> document.put(substringBefore(el.field().path(), "."),
                        substringAfter(el.field().path(), "."), el.field().value());
            }
        });
        return JsonUtils.toObject(document.jsonString(), model);
    }

    @SneakyThrows
    public static JsonNode toJsonNode(final String json) {
        return MAPPER.readTree(json);
    }

    @SneakyThrows
    public static JsonNode toJsonNode(final Object json) {
        final String stringValue = MAPPER.writeValueAsString(json);
        return MAPPER.readTree(stringValue);
    }

    @SneakyThrows
    public static <T> T toObject(String json, Class<T> type) {
        return MAPPER.readValue(json, type);
    }

    @SneakyThrows
    public static <T> T toObject(Map<String, String> map, Class<T> type) {
        return MAPPER.convertValue(map, type);
    }

    @SneakyThrows
    public static <T> T toObject(String jsonData, String path, Class<T> type) {
        HashMap<String, String> map = JsonPath.read(jsonData, path);
        return JsonUtils.toObject(map, type);
    }

    public static <T> T updateObjectFields(Object initObject, Map<String, Object> fieldsToUpdate,
                                           Class<T> model) {
        DocumentContext documentContext = JsonPath.parse(JsonUtils.toJson(initObject));
        for (var entry : fieldsToUpdate.entrySet()) {
            documentContext.set("$." + entry.getKey(), entry.getValue());
        }
        return JsonUtils.toObject(documentContext.jsonString(), model);
    }

    public static String prettyPrint(String json) {
        String output = json;
        try {
            if (StringUtils.isNotBlank(json)) {
                output = MAPPER.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(MAPPER.readValue(json, Object.class));
            }
        } catch (IOException e) {
            log.warn("Failed to print string as pretty JSON", e);
        }
        return output;
    }

}