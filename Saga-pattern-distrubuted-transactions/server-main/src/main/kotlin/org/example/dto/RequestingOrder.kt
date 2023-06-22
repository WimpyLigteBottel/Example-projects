package org.example.dto

import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*


data class RequestingOrder(
    var id: String = UUID.randomUUID().toString(),
    var pendingActions: MutableList<ActionAndState> = mutableListOf(),
    var created: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC),
    var updated: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC)
) {

    fun isSuccess(): Boolean {
        return pendingActions.count { it.state == State.SUCCESS } == pendingActions.size
    }

    fun isFailure(): Boolean {
        return pendingActions.count { it.state == State.FAILED } == pendingActions.size
    }


    fun hasFailure(): Boolean {
        return pendingActions.count { it.state == State.FAILED } >= 1
    }
}


data class ActionAndState(var id: String? = null, var name: String, var state: State = State.PENDING)


enum class State {
    PENDING,
    SUCCESS,
    FAILED
}