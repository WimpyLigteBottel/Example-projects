package com.example.kotlin_ecs_horde_base_moment.movement

import com.example.kotlin_ecs_horde_base_moment.World
import kotlin.math.sqrt

object PlayerMovementSystem {

    fun update(world: World) {
        for (player in world.players) {

            val position = world.positions[player] ?: continue
            val velocity = world.velocities[player] ?: continue
            val speed = world.speeds[player] ?: continue
            val target = world.targets[player] ?: continue

            val targetPosition = world.positions[target.entity] ?: continue

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