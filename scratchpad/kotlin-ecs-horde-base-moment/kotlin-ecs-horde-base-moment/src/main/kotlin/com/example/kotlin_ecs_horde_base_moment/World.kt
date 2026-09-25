package com.example.kotlin_ecs_horde_base_moment

import com.example.kotlin_ecs_horde_base_moment.movement.MovementSystem
import com.example.kotlin_ecs_horde_base_moment.movement.PlayerMovementSystem
import com.example.kotlin_ecs_horde_base_moment.movement.ZombieMovementSystem
import com.example.kotlin_ecs_horde_base_moment.state.StateChecker


typealias Entity = Int

data class Position(
    var x: Float,
    var y: Float
)

data class Velocity(
    var x: Float,
    var y: Float
)

data class Speed(
    val value: Float
)

data class Target(
    val entity: Entity
)

enum class UnitType {
    ZOMBIE,
    PLAYER_1
}

class World {

    private var nextEntityId = 0

    var width = 100
    var height = 100

    val positions = mutableMapOf<Entity, Position>()
    val velocities = mutableMapOf<Entity, Velocity>()
    val speeds = mutableMapOf<Entity, Speed>()
    val targets = mutableMapOf<Entity, Target>()
    val entityType = mutableMapOf<Entity, UnitType>()
    val zombies = mutableSetOf<Entity>()
    val players = mutableSetOf<Entity>()

    fun createEntity(): Entity {
        return nextEntityId++
    }


    fun tick(deltaTime: Float) {

        // DO movements
        ZombieMovementSystem.update(this)
        PlayerMovementSystem.update(this)
        MovementSystem.update(this, deltaTime)


        //Game over check
        if (StateChecker.zombieTouchesPlayer(this)) {
            throw RuntimeException("GAME OVER")
        }
    }

    fun displayWorld() {
        for (y in 0 until height) {

            for (x in 0 until width) {

                var character = '.'

                // Check if the player is here
                val player = players.firstOrNull()

                if (player != null) {
                    val playerPosition = positions[player]

                    if (
                        playerPosition != null &&
                        playerPosition.x.toInt() == x &&
                        playerPosition.y.toInt() == y
                    ) {
                        character = 'P'
                    }
                }

                // Check if a zombie is here
                for (zombie in zombies) {

                    val zombiePosition = positions[zombie]

                    if (
                        zombiePosition != null &&
                        zombiePosition.x.toInt() == x &&
                        zombiePosition.y.toInt() == y
                    ) {
                        character = 'Z'
                    }
                }

                print(character)
            }

            println()
        }
    }


}