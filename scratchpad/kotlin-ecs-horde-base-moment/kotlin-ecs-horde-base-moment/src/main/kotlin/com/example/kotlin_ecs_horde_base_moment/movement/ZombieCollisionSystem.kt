package com.example.kotlin_ecs_horde_base_moment.collision

import com.example.kotlin_ecs_horde_base_moment.Entity
import com.example.kotlin_ecs_horde_base_moment.World
import kotlin.math.sqrt

object ZombieCollisionSystem {

    private const val COLLISION_DISTANCE = 2f

    fun update(world: World) {

        val zombies = world.zombies.toList()

        for (i in zombies.indices) {

            val zombieA = zombies[i]
            val positionA =
                world.positions[zombieA] ?: continue

            for (j in i + 1 until zombies.size) {

                val zombieB = zombies[j]
                val positionB =
                    world.positions[zombieB] ?: continue

                val dx = positionB.x - positionA.x
                val dy = positionB.y - positionA.y

                val distance = sqrt(dx * dx + dy * dy)

                if (distance < COLLISION_DISTANCE) {
                    separate(
                        world,
                        zombieA,
                        zombieB,
                        dx,
                        dy,
                        distance
                    )
                }
            }
        }
    }

    private fun separate(
        world: World,
        zombieA: Entity,
        zombieB: Entity,
        dx: Float,
        dy: Float,
        distance: Float
    ) {

        val positionA =
            world.positions[zombieA] ?: return

        val positionB =
            world.positions[zombieB] ?: return

        // Zombies are exactly on top of each other.
        if (distance == 0f) {
            positionA.x -= 0.01f
            positionB.x += 0.01f
            return
        }

        val overlap = COLLISION_DISTANCE - distance

        val normalX = dx / distance
        val normalY = dy / distance

        // Push each zombie away by half the overlap.
        val pushX = normalX * overlap / 2f
        val pushY = normalY * overlap / 2f

        positionA.x -= pushX
        positionA.y -= pushY

        positionB.x += pushX
        positionB.y += pushY
    }
}