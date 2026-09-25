package com.example.kotlin_ecs_horde_base_moment


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


    private fun updateZombieMovement(world: World) {

        for (zombie in world.zombies) {

            val position = world.positions[zombie] ?: continue
            val velocity = world.velocities[zombie] ?: continue
            val speed = world.speeds[zombie] ?: continue
            val target = world.targets[zombie] ?: continue

            val targetPosition =
                world.positions[target.entity] ?: continue

            val dx = targetPosition.x - position.x
            val dy = targetPosition.y - position.y

            val distance = kotlin.math.sqrt(
                dx * dx + dy * dy
            )

            if (distance > 0f) {

                velocity.x = dx / distance * speed.value
                velocity.y = dy / distance * speed.value
            }
        }
    }

    private fun updateMovement(world: World, deltaTime: Float) {
        for ((entity, velocity) in world.velocities) {

            val position = world.positions[entity]
                ?: continue

            position.x += velocity.x * deltaTime
            position.y += velocity.y * deltaTime
        }
    }


    fun update(deltaTime: Float) {
        updateZombieMovement(this)
        updateMovement(this, deltaTime)
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