package org.example.dateprint;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * <p>
 * This will printout the current execution timestamp
 * </p>
 *
 * @timestamp
 */
@Mojo(name = "timestamp", defaultPhase = LifecyclePhase.VALIDATE, threadSafe = true)
public class PrintStartDate extends AbstractMojo {


    /**
     * <p>Indicates whether the time should be printed or not else it will just be date</p>
     * Example:
     * <br/>
     * <br/>
     * true | 2024-02-19T01:00:321Z
     * <br/>
     * false | 2024-02-19
     */
    @Parameter(
            property = "withTime",
            defaultValue = "false"
    )
    boolean withTime;


    public void execute() {

        if (withTime) {
            System.out.println(OffsetDateTime.now());
        } else {
            System.out.println(LocalDate.now());
        }

    }
}
