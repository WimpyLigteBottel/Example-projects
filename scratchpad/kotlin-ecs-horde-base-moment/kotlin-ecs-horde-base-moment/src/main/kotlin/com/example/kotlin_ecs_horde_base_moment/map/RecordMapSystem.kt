package com.example.kotlin_ecs_horde_base_moment.map

import com.example.kotlin_ecs_horde_base_moment.World
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

object RecordMapSystem {
    fun record(world: World) {
        val map = DisplaySystem.displayWorld(world)


        val name: String = world.seed.toString()
        val path =
            "C:\\code\\Example-projects\\scratchpad\\kotlin-ecs-horde-base-moment\\kotlin-ecs-horde-base-moment\\target\\$name"

        var filePath = Path.of(path)

        Files.writeString(
            filePath,
            map + "\n",
            StandardOpenOption.APPEND,
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE
        )


    }

}