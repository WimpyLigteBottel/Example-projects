package org.example.combine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class YamlCombinerTest {

    String petYaml = "src/test/resources/pets.yaml";
    String storeYaml = "src/test/resources/store.yaml";
    String usersYaml = "src/test/resources/users.yaml";


    @Test
    @DisplayName("Can handle 1 input file only")
    void handleOneFileOnly() {
        var inputPaths = List.of(
                petYaml
        );

        var yamlCombiner = new YamlCombiner(inputPaths, "./target/output1.yaml");

        yamlCombiner.execute();

    }


    @Test
    @DisplayName("Can handle 2 input files")
    void handleMultipleFiles() {
        var inputPaths = List.of(
                petYaml,
                storeYaml
        );

        var yamlCombiner = new YamlCombiner(inputPaths, "./target/output2.yaml");

        yamlCombiner.execute();
    }

    @Test
    @DisplayName("Can handle 3 input files")
    void handleXMultipleFiles() {
        var inputPaths = List.of(
                petYaml,
                storeYaml,
                usersYaml
        );

        var yamlCombiner = new YamlCombiner(inputPaths, "./target/output3.yaml");

        yamlCombiner.execute();
    }

}