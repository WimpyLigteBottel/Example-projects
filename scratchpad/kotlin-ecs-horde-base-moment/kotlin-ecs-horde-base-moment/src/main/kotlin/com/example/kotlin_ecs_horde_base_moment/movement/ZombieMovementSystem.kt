package com.example.kotlin_ecs_horde_base_moment.movement

import com.example.kotlin_ecs_horde_base_moment.World
import kotlin.math.sqrt


object ZombieMovementSystem {

    fun update(world: World) {

        for (zombie in world.zombies) {

            val position = world.positions[zombie] ?: continue
            val velocity = world.velocities[zombie] ?: continue
            val speed = world.speeds[zombie] ?: continue
            val target = world.targets[zombie] ?: continue

            val targetPosition =
                world.positions[target.entity] ?: continue

            val dx = targetPosition.x - position.x
            val dy = targetPosition.y - position.y

            val distance = sqrt(
                dx * dx + dy * dy
            )

            if (distance > 0f) {

                velocity.x = dx / distance * speed.value
                velocity.y = dy / distance * speed.value
            }
        }
    }
}