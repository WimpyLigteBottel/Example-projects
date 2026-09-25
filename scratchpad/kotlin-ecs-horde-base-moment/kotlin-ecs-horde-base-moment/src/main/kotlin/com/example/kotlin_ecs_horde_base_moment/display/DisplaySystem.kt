package com.example.kotlin_ecs_horde_base_moment.display

import com.example.kotlin_ecs_horde_base_moment.World



object DisplaySystem {

    enum class UnitCharacter(val tile: String) {
        ZOMBIE("Z"),
        PLAYER("P"),
        EMPTY(".")
    }


    fun displayWorld(world: World) {
        for (y in 0 until world.height) {

            for (x in 0 until world.width) {

                var character = UnitCharacter.EMPTY

                // Check if the player is here
                val player = world.players.firstOrNull()

                if (player != null) {
                    val playerPosition = world.positions[player]

                    if (
                        playerPosition != null &&
                        playerPosition.x.toInt() == x &&
                        playerPosition.y.toInt() == y
                    ) {
                        character = UnitCharacter.PLAYER
                    }
                }

                // Check if a zombie is here
                for (zombie in world.zombies) {

                    val zombiePosition = world.positions[zombie]

                    if (
                        zombiePosition != null &&
                        zombiePosition.x.toInt() == x &&
                        zombiePosition.y.toInt() == y
                    ) {
                        character = UnitCharacter.ZOMBIE
                    }
                }

                print(character.tile)
            }

            println()
        }
    }

}