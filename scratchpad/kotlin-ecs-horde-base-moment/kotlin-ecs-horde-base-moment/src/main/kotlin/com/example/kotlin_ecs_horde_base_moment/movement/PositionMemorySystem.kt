package com.example.kotlin_ecs_horde_base_moment.movement

import com.example.kotlin_ecs_horde_base_moment.World

object PositionMemorySystem {

    fun update(world: World) {
        for (player in world.players) {
            val position = world.positions[player] ?: continue

            world.positionMemory[player]?.add(position.copy())
        }
    }

}