package com.example.kotlin_ecs_horde_base_moment.display

import com.example.kotlin_ecs_horde_base_moment.World


object DisplaySystem {

    enum class UnitCharacter(val tile: String) {
        ZOMBIE("Z"),
        PLAYER("P"),
        EMPTY(" "),
        BORDER("X"),
        WALKED_TILE("."),
    }

    fun displayWorld(world: World) {
        val map = StringBuilder()


        for (y in -1 until world.height + 2) {
            for (x in -1 until world.width + 2) {

                var character = UnitCharacter.EMPTY

                if (x < 0 || y < 0 || x > world.width || y > world.height) {
                    character = UnitCharacter.BORDER
                }

                character = updatePlayerPosition(world, x, y, character)
                character = updateZombieLocationsCharacter(world, x, y, character)
                character = updatePastWalkedLocations(world, x, y, character)

                map.append(character.tile)
            }
            map.append("\n")
        }

        print(map)
    }

    private fun updatePlayerPosition(
        world: World,
        x: Int,
        y: Int,
        character: UnitCharacter
    ): UnitCharacter {
        // Check if the player is here
        var character1 = character
        val player = world.players.firstOrNull()

        if (player != null) {
            val playerPosition = world.positions[player]

            if (
                playerPosition != null &&
                playerPosition.x.toInt() == x &&
                playerPosition.y.toInt() == y
            ) {
                character1 = UnitCharacter.PLAYER
            }
        }
        return character1
    }

    private fun updateZombieLocationsCharacter(
        world: World,
        x: Int,
        y: Int,
        character: UnitCharacter
    ): UnitCharacter {

        if (UnitCharacter.EMPTY != character) {
            return character
        }

        // Check if a zombie is here
        var character1 = character
        for (zombie in world.zombies) {

            val zombiePosition = world.positions[zombie]

            if (
                zombiePosition != null &&
                zombiePosition.x.toInt() == x &&
                zombiePosition.y.toInt() == y
            ) {
                character1 = UnitCharacter.ZOMBIE
            }
        }


        return character1
    }

    private fun updatePastWalkedLocations(
        world: World,
        x: Int,
        y: Int,
        character: UnitCharacter,
    ): UnitCharacter {

        if (UnitCharacter.EMPTY != character) {
            return character
        }

        var character1 = character
        val playerPastPositions = world.positionMemory
            .filter { world.players.contains(it.key) }
            .firstNotNullOf { it.value }

        val positionsInFloats = playerPastPositions.map { it.x.toInt() to it.y.toInt() }
        val hasSteppedHereBefore = positionsInFloats.contains(x to y)

        if (hasSteppedHereBefore) {
            character1 = UnitCharacter.WALKED_TILE
        }
        return character1
    }

}