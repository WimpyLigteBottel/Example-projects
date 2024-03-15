package org.example.combine;

import org.example.combine.utils.MapMerge;
import org.example.combine.utils.YamlUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class YamlCombiner {


    public void createCombinedYaml(String outputPath, List<String> specs) {
        try {
            Path newOutputPath = Path.of(outputPath);


            String textToOutput = getCombinedYamlOuputAsString(specs);


            Files.writeString(newOutputPath, textToOutput, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String getCombinedYamlOuputAsString(List<String> specs) {
        return specs.stream()
                .map(YamlUtil::loadFile)
                .reduce(MapMerge::deepMerge) // merge files 1 by 1
                .map(this::sortToOpenApiStyle)
                .map(YamlUtil::toYamlString)
                .orElseThrow();
    }


    /**
     * The map will be sorted in semi readable way for open api specs.
     * <p></p>
     * <p>
     * Example:
     * base properties
     * paths
     * components
     */
    public Map<String, Object> sortToOpenApiStyle(Map<String, Object> mapToBeSorted) {
        //This
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>(mapToBeSorted);

        linkedHashMap.remove("paths");
        linkedHashMap.put("paths", mapToBeSorted.get("paths"));

        linkedHashMap.remove("components");
        linkedHashMap.put("components", mapToBeSorted.get("components"));
        return linkedHashMap;
    }

}
