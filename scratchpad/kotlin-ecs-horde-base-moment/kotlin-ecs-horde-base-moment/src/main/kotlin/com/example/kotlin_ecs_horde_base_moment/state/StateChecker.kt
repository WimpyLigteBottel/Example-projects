package com.example.kotlin_ecs_horde_base_moment.state

import com.example.kotlin_ecs_horde_base_moment.World

object StateChecker {

    fun zombieTouchesPlayer(world: World): Boolean {
        val player = world.players.firstOrNull() ?: return false
        val playerPosition = world.positions[player] ?: return false

        for (zombie in world.zombies) {

            val zombiePosition = world.positions[zombie] ?: continue

            val dx = zombiePosition.x - playerPosition.x
            val dy = zombiePosition.y - playerPosition.y

            val distanceSquared = dx * dx + dy * dy

            if (distanceSquared < 1f) {
                return true
            }
        }

        return false
    }
}