package org.example;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * <p>
 *     This calls will printout your custom message
 * </p>
 *
 * @name custom-message
 */
@Mojo(name = "custom-message", defaultPhase = LifecyclePhase.VALIDATE)
public class CustomMessage extends AbstractMojo {

    /**
     * <p>The custom message you would like to print</p>
     */
    @Parameter(
            property = "customMessage",
            defaultValue = "customMessage"
    )
    String customMessage;


    public void execute() {
        System.out.println(customMessage);
    }
}
