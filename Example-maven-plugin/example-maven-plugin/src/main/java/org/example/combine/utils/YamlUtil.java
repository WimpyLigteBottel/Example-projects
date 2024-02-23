package org.example.combine.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class YamlUtil {


    private static final ObjectMapper jsonObjectMapper = new ObjectMapper();
    private static final ObjectMapper yamlObjectMapper = new ObjectMapper(new YAMLFactory());


    /**
     * Transform the input to json string.
     * <p></p>
     * Example:
     * <p/>
     * {"info":{"name":"b"}}
     *
     * @param input the object you want to transform to json format
     * @return {@link String}
     */

    public static String toJson(Object input) {
        try {
            return jsonObjectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Transform the input to json string.
     * <p></p>
     * Example:
     * <p/>
     * {"info":{"name":"b"}}
     *
     * @param input the object you want to transform to json format
     * @return {@link String}
     */

    public static String toYaml(Object input) {
        try {
            return yamlObjectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
