package org.example.combine;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.example.combine.utils.MapMerge;
import org.example.combine.utils.YamlUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * This will read the specified yaml specs and combine it into combined yaml
 */
@Mojo(name = "combine", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class YamlCombiner extends AbstractMojo {


    YamlCombiner() {
    }

    YamlCombiner(List<String> specs, String ouputPath) {
        this.specs = specs;
        this.ouputPath = ouputPath;
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


        var combinedMap = specs
                .stream()
                .map(path -> YamlUtil.loadFile(path))
                .reduce(MapMerge::deepMerge) // merge files 1 by 1
                .map(YamlUtil::sortToOpenApiStyle)
                .map(YamlUtil::toYamlString)
                .get();


        Path newOutputPath = Path.of(ouputPath);

        try {
            Files.write(newOutputPath, combinedMap.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //

}
