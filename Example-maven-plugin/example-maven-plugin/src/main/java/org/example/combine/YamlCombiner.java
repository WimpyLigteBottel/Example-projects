package org.example.combine;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.example.combine.models.YamlFile;
import org.example.combine.utils.YamlUtil;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * This will read the specified yaml specs and combine it into combined yaml
 */
@Mojo(name = "combine", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class YamlCombiner extends AbstractMojo {


    /**
     * This should be path to your spec files
     * <p/>
     * Example:
     * <p/>
     * Windows: D:\coding\temp\your\yaml.yaml
     * Linux based: /users/xxx/xxx/yaml
     */
    @Parameter(
            name = "specs",
            required = true
    )
    private List<String> specs;


    /**
     * What is the output file that you would like to create
     * <p/>
     * Example:
     * <p/>
     * The default value will be `combined.yaml`
     */
    @Parameter(
            name = "output",
            required = true,
            defaultValue = "combined.yaml"
    )
    private String output;


    @Override
    public void execute() {
        System.out.println("Starting the combining phase");

        Yaml yaml = new Yaml();

        System.out.println("GOING TO PRINT FILE AS DIFFERENT OUTPUTS");
        specs.forEach(path -> {
            try {
                var inputStream = Files.newInputStream(Path.of(path));
                var yamlFile = yaml.loadAs(inputStream, YamlFile.class);

                System.out.println("X".repeat(10));
                System.out.println(YamlUtil.toJson(yamlFile));
                System.out.println(YamlUtil.toYaml(yamlFile));
                System.out.println("X".repeat(10));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        System.out.println("output " + output);

    }
}
