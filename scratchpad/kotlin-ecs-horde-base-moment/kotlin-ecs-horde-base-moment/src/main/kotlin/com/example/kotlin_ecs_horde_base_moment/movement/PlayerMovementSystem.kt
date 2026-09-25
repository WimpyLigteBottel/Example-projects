package com.example.kotlin_ecs_horde_base_moment.movement

import com.example.kotlin_ecs_horde_base_moment.Direction
import com.example.kotlin_ecs_horde_base_moment.Entity
import com.example.kotlin_ecs_horde_base_moment.World

object PlayerMovementSystem {

    fun update(world: World) {

        for (player in world.players) {
            val velocity = world.velocities[player] ?: continue
            val speed = world.speeds[player] ?: continue


            val direction = calculateFleeDirection(world, player)

            velocity.x = direction.x * speed.value
            velocity.y = direction.y * speed.value
        }
    }

    private fun calculateFleeDirection(
        world: World,
        player: Entity
    ): Direction {

        val position =
            world.positions[player] ?: return Direction(0f, 0f)

        var fleeX = 0f
        var fleeY = 0f

        for (zombie in world.zombies) {

            val zombiePosition =
                world.positions[zombie] ?: continue

            val dx = position.x - zombiePosition.x
            val dy = position.y - zombiePosition.y

            val distanceSquared = dx * dx + dy * dy

            if (distanceSquared == 0f) continue

            val distance = kotlin.math.sqrt(distanceSquared)
            val strength = 1f / distanceSquared

            fleeX += dx / distance * strength
            fleeY += dy / distance * strength
        }

        // Push away from boundaries.
        val boundaryDistance = 15f
        val boundaryStrength = 0.1f

        if (position.x < boundaryDistance) {
            fleeX += (boundaryDistance - position.x) /
                    boundaryDistance * boundaryStrength
        }

        if (position.x > world.width - boundaryDistance) {
            fleeX -= (position.x - (world.width - boundaryDistance)) /
                    boundaryDistance * boundaryStrength
        }

        if (position.y < boundaryDistance) {
            fleeY += (boundaryDistance - position.y) /
                    boundaryDistance * boundaryStrength
        }

        if (position.y > world.height - boundaryDistance) {
            fleeY -= (position.y - (world.height - boundaryDistance)) /
                    boundaryDistance * boundaryStrength
        }

        return normalize(
            Direction(fleeX, fleeY)
        )
    }

    private fun normalize(direction: Direction): Direction {

        val length = kotlin.math.sqrt(
            direction.x * direction.x +
                    direction.y * direction.y
        )

        if (length == 0f) {
            return Direction(0f, 0f)
        }

        return Direction(
            direction.x / length,
            direction.y / length
        )
    }

}