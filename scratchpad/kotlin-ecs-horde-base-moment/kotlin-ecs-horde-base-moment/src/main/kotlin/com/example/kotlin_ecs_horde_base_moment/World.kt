package com.example.kotlin_ecs_horde_base_moment

import com.example.kotlin_ecs_horde_base_moment.collision.ZombieCollisionSystem
import com.example.kotlin_ecs_horde_base_moment.map.RecordMapSystem
import com.example.kotlin_ecs_horde_base_moment.movement.MovementSystem
import com.example.kotlin_ecs_horde_base_moment.movement.PlayerMovementSystem
import com.example.kotlin_ecs_horde_base_moment.movement.PositionMemorySystem
import com.example.kotlin_ecs_horde_base_moment.movement.ZombieMovementSystem
import com.example.kotlin_ecs_horde_base_moment.state.StateChecker
import kotlin.random.Random


typealias Entity = Int

data class Direction(
    val x: Float,
    val y: Float
)

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

    var width = 30
    var height = 30

    val positions = mutableMapOf<Entity, Position>()
    val positionMemory = mutableMapOf<Entity, MutableList<Position>>()
    val velocities = mutableMapOf<Entity, Velocity>()
    val speeds = mutableMapOf<Entity, Speed>()
    val targets = mutableMapOf<Entity, Target>()
    val entityType = mutableMapOf<Entity, UnitType>()
    val zombies = mutableSetOf<Entity>()
    val players = mutableSetOf<Entity>()
    var tickNumber = 0

    var seed = Random.nextInt(0, 10000000)
    var random = Random(seed)

    fun createEntity(): Entity {
        return nextEntityId++
    }


    fun tick(deltaTime: Float, tickspeed: Long) {

        // DO movements
        PositionMemorySystem.update(this)
        ZombieMovementSystem.update(this)
        PlayerMovementSystem.update(this)
        MovementSystem.update(this, deltaTime)
        ZombieCollisionSystem.update(this)

        RecordMapSystem.record(this)
        //Game over check
        if (StateChecker.zombieTouchesPlayer(this)) {
            throw RuntimeException("GAME OVER")
        }


        tickNumber++
        Thread.sleep(tickspeed)

    }


}