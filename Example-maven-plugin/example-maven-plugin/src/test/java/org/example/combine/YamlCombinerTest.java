package org.example.combine;

import org.junit.jupiter.api.Test;

import java.util.List;

class YamlCombinerTest {


    @Test
    void checkIfFailGetsCombined() {
        var inputPaths = List.of(
                "src/test/resources/testA.yaml",
                "src/test/resources/testB.yaml"
        );

        var yamlCombiner = new YamlCombiner(inputPaths, "./target/output.yaml");

        yamlCombiner.execute();

    }

}