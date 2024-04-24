package com.template.qa.cucumber;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@Component
public class DataMapper {

    private static final String PARAM_PATTERN = "%([^%]+)%";

    private Map<String, String> envVariables = new HashMap<>();

    public String resolve(String str) {
        if (str == null) {
            return null;
        }
        Matcher matcher = Pattern.compile(PARAM_PATTERN).matcher(str);
        String resolved = str;
        while (matcher.find()) {
            String parameterName = matcher.group(1);
            if (envVariables.containsKey(parameterName)) {
                resolved = resolved.replaceFirst(matcher.group(0), envVariables.get(parameterName));
            }
        }
        return resolved;
    }

    public List<String> resolve(List<String> list) {
        return list.stream().map(this::resolve).toList();
    }

    public Map<String, String> resolve(Map<String, String> table) {
        return table.entrySet().stream().collect(
                HashMap::new,
                (map, entry) -> map.put(entry.getKey(), resolve(entry.getValue())),
                HashMap::putAll);
    }
}
