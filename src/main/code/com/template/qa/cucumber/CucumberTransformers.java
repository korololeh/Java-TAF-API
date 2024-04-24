package com.template.qa.cucumber;

import static org.apache.commons.lang3.StringUtils.endsWith;
import static org.apache.commons.lang3.StringUtils.startsWith;
import static org.apache.commons.lang3.StringUtils.strip;
import static wiremock.org.apache.commons.lang3.EnumUtils.getEnum;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.wnameless.json.unflattener.JsonUnflattener;
import com.template.qa.utils.json.JsonModifier;
import com.template.qa.utils.json.JsonUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.DefaultDataTableCellTransformer;
import io.cucumber.java.DefaultDataTableEntryTransformer;
import io.cucumber.java.DefaultParameterTransformer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("unused")
public class CucumberTransformers {

    @Autowired
    private DataMapper dataMapper;

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);

    @DefaultParameterTransformer
    @DefaultDataTableCellTransformer(replaceWithEmptyString = {"[blank]"})
    public Object defaultTransformer(String fromValue, Type toValueType) {
        return MAPPER
                .convertValue(dataMapper.resolve(fromValue), MAPPER.constructType(toValueType));
    }

    @DefaultDataTableEntryTransformer(replaceWithEmptyString = {"[blank]"})
    public Object tableTransformer(Map<String, String> table, Type toValueType) {
        return transform(dataMapper.resolve(table), TypeFactory.rawClass(toValueType));
    }

    public <T> T transform(Map<String, String> table, Class<T> type) {
        var map = table.entrySet().stream().collect(
                HashMap::new,
                (m, entry) -> m.put(entry.getKey(), processValue(entry.getValue())),
                HashMap::putAll);
        try {
            String json = JsonUnflattener.unflatten(JsonUtils.toJson(map));
            return MAPPER.readerFor(type).readValue(json);
        } catch (IOException e) {
            throw new CucumberFrameworkException(
                    "Fail to parse cucumber table into instance of " + type, e);
        }
    }

    @SneakyThrows
    @DataTableType(replaceWithEmptyString = {"[blank]"})
    public List<JsonModifier> modifiers(DataTable modifiers) {
        return modifiers.cells().stream()
                .map(row -> new JsonModifier(getEnum(JsonModifier.Operation.class, row.get(0)),
                        new JsonModifier.Field(row.get(1), row.get(2)))).toList();
    }

    @DataTableType(replaceWithEmptyString = {"[blank]"})
    public Object processCellValue(String cell) {
        return switch (cell) {
            case "[null]" -> null;
            case "[]" -> Collections.emptyList();
            case "[false]" -> false;
            case "[true]" -> true;
            default -> cell;
        };
    }

    /**
     * Apply additional transformation logic for table value
     */
    private Object processValue(final String value) {
        final String nullAwareValue = processCellValue(value).toString();
        final String resolvedValue = dataMapper.resolve(nullAwareValue);
        if (startsWith(resolvedValue, "{") && endsWith(resolvedValue, "}")) {
            return JsonUtils.toObject(resolvedValue, Object.class);
        }
        if (startsWith(resolvedValue, "[") && endsWith(resolvedValue, "]")) {
            if (resolvedValue.contains("\"")) {
                return JsonUtils.toObject(resolvedValue, List.class);
            } else {
                return strip(resolvedValue, "[]").split(", *");
            }
        }
        return resolvedValue;
    }
}