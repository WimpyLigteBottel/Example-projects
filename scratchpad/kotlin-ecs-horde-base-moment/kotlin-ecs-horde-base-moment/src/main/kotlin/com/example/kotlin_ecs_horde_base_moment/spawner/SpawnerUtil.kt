package com.example.kotlin_ecs_horde_base_moment.spawner

import com.example.kotlin_ecs_horde_base_moment.*
import com.example.kotlin_ecs_horde_base_moment.Target
import kotlin.random.Random


fun spawnWorld(): World {
    val world = World()
    world.height = 20
    world.width = 100
    world.seed = Random.nextInt(0, 1000)
    world.random = Random(world.seed)

    spawnPlayer(world)

    repeat(world.random.nextInt(1, 20)) {
        spawnZombie(world, it.toFloat(), it.toFloat())
    }


    return world

}

fun spawnPlayer(world: World): World {
    val player = world.createEntity()

    world.players.add(player)
    world.positions[player] = Position(x = 0f, y = 0f)
    world.velocities[player] = Velocity(x = 0f, y = 0f)
    world.entityType[player] = UnitType.PLAYER_1
    world.positionMemory[player] = mutableListOf(Position(x = 0f, y = 0f))
    world.speeds[player] = Speed(2f)


    return world
}


fun spawnZombie(world: World, x: Float, y: Float): World {
    val zombieId = world.createEntity()

    world.zombies.add(zombieId)
    world.positions[zombieId] = Position(x = x + world.random.nextInt(5, 20), y = y + world.random.nextInt(5, 20))
    world.velocities[zombieId] = Velocity(x = 0f, y = 0f)
    world.speeds[zombieId] = Speed(value = 1f)
    // targets the first player
    world.players.firstOrNull()?.let { world.targets[zombieId] = Target(entity = world.players.first()) }
    world.entityType[zombieId] = UnitType.ZOMBIE

    return world
}
