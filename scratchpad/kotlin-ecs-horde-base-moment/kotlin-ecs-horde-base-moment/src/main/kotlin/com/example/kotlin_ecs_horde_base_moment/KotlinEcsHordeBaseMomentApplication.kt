package com.example.kotlin_ecs_horde_base_moment

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
class Startup() : CommandLineRunner {
    override fun run(vararg args: String) {

        val world = World()
        world.height = 50
        world.width = 50

        spawnPlayer(world)

        repeat(1) {
            spawnZombie(world, it.toFloat(), it.toFloat())
        }



        repeat(10) {
            println("")
            world.update(1f)
            world.displayWorld()
            Thread.sleep(2000)
        }

    }

}