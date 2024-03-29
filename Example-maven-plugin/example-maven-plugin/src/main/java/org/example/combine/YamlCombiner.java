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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

    public String getCombinedYamlOuputAsString(List<String> specs) {
        return specs.stream()
                .map(YamlUtil::loadFile)
                .reduce(MapMerge::deepMerge) // merge files 1 by 1
                .map(this::sortToOpenApiStyle)
                .map(YamlUtil::toYamlString)
                .map(text -> replaceRefTags(text, specs)) // cleans up ref tags
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

        // dont add null values if fields do not exist
        if (mapToBeSorted.get("paths") != null) {
            linkedHashMap.remove("paths");
            linkedHashMap.put("paths", mapToBeSorted.get("paths"));
        }

        // dont add null values if fields do not exist
        if (mapToBeSorted.get("components") != null) {
            linkedHashMap.remove("components");
            linkedHashMap.put("components", mapToBeSorted.get("components"));
        }

        return linkedHashMap;
    }


    /**
     * This will clean up the refs tags after everything has been combined
     * Example:
     * <p></p>
     * from:
     * $ref: './common.yaml#/components/schemas/Error'
     * <p></p>
     * to:
     * $ref: '#/components/schemas/Error'
     */
    public String replaceRefTags(String text, List<String> specs) {
        if (specs.size() == 1)
            return text;

        var tempFile = text;

        List<String> formattedReferences = specs
                .stream()
                .map(x -> "." + x.substring(x.lastIndexOf("/")))
                .toList();

        Pattern pattern = Pattern.compile("\\.(.*?)#");
        Matcher matcher = pattern.matcher(tempFile);


        while (matcher.find()) {
            var group = matcher.group();

            var isMatchAppearingInSpecList = formattedReferences
                    .stream()
                    .anyMatch(filepath -> filepath.contains(group.substring(group.lastIndexOf("/"), group.length() - 1)));

            if (isMatchAppearingInSpecList) {
                tempFile = tempFile.replaceAll(group, "#");
            }


        }

        return tempFile;
    }

}
