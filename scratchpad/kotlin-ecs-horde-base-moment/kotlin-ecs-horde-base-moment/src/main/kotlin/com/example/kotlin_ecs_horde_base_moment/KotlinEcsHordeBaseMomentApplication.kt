package com.example.kotlin_ecs_horde_base_moment

import com.example.kotlin_ecs_horde_base_moment.spawner.spawnWorld
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component

@SpringBootApplication
class KotlinEcsHordeBaseMomentApplication

fun main(args: Array<String>) {
    runApplication<KotlinEcsHordeBaseMomentApplication>(*args)
}


@Component
class Startup : CommandLineRunner {
    override fun run(vararg args: String) {
        val world = spawnWorld()
        while (true) {
            println("")
            world.tick(1f)
            Thread.sleep(1000)
        }
    }

}