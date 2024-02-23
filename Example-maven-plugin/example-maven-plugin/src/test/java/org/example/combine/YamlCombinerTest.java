package org.example.combine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class YamlCombinerTest {

    @Test
    @DisplayName("Can handle 1 input file only")
    void handleOneFileOnly() {
        var inputPaths = List.of(
                "src/test/resources/testA.yaml"
        );

        var yamlCombiner = new YamlCombiner(inputPaths, "./target/output1.yaml");

        yamlCombiner.execute();

    }


    @Test
    @DisplayName("Can handle 2 input files")
    void handleMultipleFiles() {
        var inputPaths = List.of(
                "src/test/resources/testA.yaml",
                "src/test/resources/testB.yaml"
        );

        var yamlCombiner = new YamlCombiner(inputPaths, "./target/output2.yaml");

        yamlCombiner.execute();
    }

    @Test
    @DisplayName("Can handle 3 input files")
    void handleXMultipleFiles() {
        var inputPaths = List.of(
                "src/test/resources/testA.yaml",
                "src/test/resources/testB.yaml",
                "src/test/resources/testC.yaml"
        );

        var yamlCombiner = new YamlCombiner(inputPaths, "./target/output3.yaml");

        yamlCombiner.execute();
    }

}