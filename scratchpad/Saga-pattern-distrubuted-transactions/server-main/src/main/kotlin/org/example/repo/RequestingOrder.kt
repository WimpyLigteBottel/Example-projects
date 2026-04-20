package org.example.repo

import org.example.api.Action
import org.example.api.PendingActionName
import org.example.api.State
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*
import kotlin.String


data class RequestingOrder(
    var id: GlobalId = UUID.randomUUID().toString(),
    var pendingActions: MutableMap<PendingActionName, Action> = mutableMapOf(),
    var created: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC),
    var updated: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC)
) {


    fun addPendingAction(action: Action) {
        pendingActions[action.name] = action
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
