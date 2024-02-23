package org.example.combine;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.example.combine.utils.MapMerge;
import org.example.combine.utils.YamlUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This will read the specified yaml specs and combine it into combined yaml
 */
@Mojo(name = "combine", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class YamlCombiner extends AbstractMojo {


    public YamlCombiner() {
    }

    public YamlCombiner(List<String> specs, String output) {
        this.specs = specs;
        this.output = output;
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

        if (specs == null || specs.isEmpty()) {
            throw new RuntimeException("specs is empty!!! you need to specify atleast 2 files");
        }


        var listOfYamlMaps = specs
                .stream()
                .map(path -> YamlUtil.loadFile(path)) // load the file to map
                .reduce((a, b) -> MapMerge.deepMerge(a, b)) // merge files 1 by 1
                .get();


        System.out.println(YamlUtil.toYaml(listOfYamlMaps));

        System.out.println("output " + output);

    }
}
