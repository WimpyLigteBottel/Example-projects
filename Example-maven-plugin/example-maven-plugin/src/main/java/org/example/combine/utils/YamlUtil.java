package org.example.combine.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class YamlUtil {

    private static final Yaml yaml = new Yaml();

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

    public static String toYamlString(Object input) {
        try {
            return yamlObjectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }




    public static <T> T loadFile(String path, Class<T> expectResponse) {
        try {
            var inputStream = Files.newInputStream(Path.of(path));

            return yaml.loadAs(inputStream, expectResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static HashMap<String, Object> loadFile(String path) {
        return loadFile(path, HashMap.class);
    }


}
