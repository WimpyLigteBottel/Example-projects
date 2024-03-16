package org.example.combine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class YamlCombinerTest {

    String common = "src/test/resources/common.yaml";
    String commonSecond = "src/test/resources/common-second.yaml";
    String usingCommon = "src/test/resources/using-common.yaml";

    @Test
    @DisplayName("multiple specs references is removed in $ref")
    void expect_multipleRefsToBeUpdated() {
        var yamlCombiner = new YamlCombiner();

        //Checks that the references exist in the file
        var before = yamlCombiner.getCombinedYamlOuputAsString(Collections.singletonList(usingCommon));

        assertTrue(before.contains("$ref: \"./common.yaml#/components/schemas/Error\""));
        assertTrue(before.contains("$ref: \"./common-second.yaml#/components/schemas/ErrorSecond\""));


        var actual = yamlCombiner.getCombinedYamlOuputAsString(List.of(common, commonSecond, usingCommon));


        //text updated
        assertTrue(actual.contains("$ref: \"#/components/schemas/Error\""));
        assertTrue(actual.contains("$ref: \"#/components/schemas/ErrorSecond\""));

        //these should not exist anymore
        assertFalse(actual.contains("$ref: \"./common.yaml#/components/schemas/Error\""));
        assertFalse(actual.contains("$ref: \"./common-second.yaml#/components/schemas/ErrorSecond\""));
    }

    @Test
    @DisplayName("./common.yaml is removed in $ref")
    void expect_refTagsToBeRemoved() {
        var yamlCombiner = new YamlCombiner();

        //Checks that the references exist in the file
        var before = yamlCombiner.getCombinedYamlOuputAsString(Collections.singletonList(usingCommon));

        assertTrue(before.contains("$ref: \"./common.yaml#/components/schemas/Error\""));
        assertTrue(before.contains("$ref: \"./common-second.yaml#/components/schemas/ErrorSecond\""));


        var actual = yamlCombiner.getCombinedYamlOuputAsString(List.of(common, usingCommon));

        //text updated
        assertTrue(actual.contains("$ref: \"#/components/schemas/Error\""));

        //these should not exist anymore
        assertFalse(actual.contains("$ref: \"./common.yaml#/components/schemas/Error\""));
    }
}