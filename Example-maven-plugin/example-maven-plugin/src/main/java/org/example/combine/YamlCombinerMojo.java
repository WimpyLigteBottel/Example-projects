package org.example.combine;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * This will read the specified yaml specs and combine it into combined yaml
 */
@Mojo(name = "combine", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class YamlCombinerMojo extends AbstractMojo {


    private final YamlCombiner yamlCombiner;


    YamlCombinerMojo(List<String> specs, String ouputPath) {
        this.specs = specs;
        this.ouputPath = ouputPath;
        this.yamlCombiner = new YamlCombiner();
    }

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
    private List<String> specs = new ArrayList<>();


    /**
     * The location where the file will be created.
     * <p/>
     * Take note that if the file exists it will be overwritten!
     * <p/>
     * Example:
     * <p/>
     * The default value will be `./combined.yaml`
     */
    @Parameter(
            name = "ouputPath",
            required = true,
            defaultValue = "./combined.yaml"
    )
    private String ouputPath;


    @Override
    public void execute() {

        if (specs == null || specs.isEmpty()) {
            throw new RuntimeException("specs is empty!!! you need to specify atleast 1 file(s)");
        }



        yamlCombiner.createCombinedYaml(ouputPath, specs);
    }


}
