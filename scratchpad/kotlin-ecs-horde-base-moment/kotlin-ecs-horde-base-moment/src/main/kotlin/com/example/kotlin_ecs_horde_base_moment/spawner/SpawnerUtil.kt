package com.example.kotlin_ecs_horde_base_moment.spawner

import com.example.kotlin_ecs_horde_base_moment.Position
import com.example.kotlin_ecs_horde_base_moment.Speed
import com.example.kotlin_ecs_horde_base_moment.Target
import com.example.kotlin_ecs_horde_base_moment.UnitType
import com.example.kotlin_ecs_horde_base_moment.Velocity
import com.example.kotlin_ecs_horde_base_moment.World


fun spawnWorld(): World {
    val world = World()
    world.height = 10
    world.width = 50

    spawnPlayer(world)

    repeat(5) {
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
    world.speeds[player] = Speed(4f)


    return world
}

fun spawnZombie(world: World, x: Float, y: Float): World {
    val zombieId = world.createEntity()

    world.zombies.add(zombieId)

    world.positions[zombieId] = Position(x = x + 10f, y = y + 10f)
    world.velocities[zombieId] = Velocity(x = 0f, y = 0f)
    world.speeds[zombieId] = Speed(value = 2f)
    // targets the first player
    world.players.firstOrNull()?.let { world.targets[zombieId] = Target(entity = world.players.first()) }
    world.entityType[zombieId] = UnitType.ZOMBIE

    return world
}
