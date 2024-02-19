package org.example;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Mojo(name = "timestamp", defaultPhase = LifecyclePhase.VALIDATE)
public class PrintStartDate extends AbstractMojo {

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
