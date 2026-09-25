package com.example.kotlin_ecs_horde_base_moment.movement

import com.example.kotlin_ecs_horde_base_moment.World
import kotlin.collections.iterator

object MovementSystem {

    fun update(world: World, deltaTime: Float) {
        for ((entity, velocity) in world.velocities) {

            val position = world.positions[entity]
                ?: continue

            position.x += velocity.x * deltaTime
            position.y += velocity.y * deltaTime
        }
    }
}