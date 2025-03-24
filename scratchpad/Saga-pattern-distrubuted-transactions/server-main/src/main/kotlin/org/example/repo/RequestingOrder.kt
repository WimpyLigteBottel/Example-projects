package org.example.repo

import org.example.Action
import org.example.PendingActionName
import org.example.State
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*


data class RequestingOrder(
    var id: String = UUID.randomUUID().toString(),
    var pendingActions: MutableMap<PendingActionName, Action> = mutableMapOf(),
    var created: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC),
    var updated: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC)
) {


    fun addPendingAction(action: Action) {
        pendingActions.put(action.name, action)
    }

    fun isSuccess(): Boolean {
        return pendingActions.count { it.value.state == State.SUCCESS } == pendingActions.size
    }

    fun isFailure(): Boolean {
        return pendingActions.count { it.value.state == State.FAILED } == pendingActions.size
    }

    fun isRolledBacked(): Boolean {
        return pendingActions.count { it.value.state == State.ROLLBACK } == pendingActions.size
    }

    fun hasPendingActions(): Boolean {
        return pendingActions.count { it.value.state == State.PENDING } >= 1
    }


    fun hasFailure(): Boolean {
        return pendingActions.count { it.value.state == State.FAILED } >= 1
    }
}
