package org.example.repo

import org.example.api.Action
import org.example.api.PendingActionName
import org.example.api.State
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*


data class RequestingOrder(
    var id: GlobalId = UUID.randomUUID().toString(),
    var pendingActions: MutableMap<PendingActionName, Action> = mutableMapOf(
        PendingActionName.CREATE_ORDER to Action(id, PendingActionName.CREATE_ORDER),
        PendingActionName.RESERVE_ITEM to Action(id, PendingActionName.RESERVE_ITEM),
        PendingActionName.PROCESS_PAYMENT to Action(id, PendingActionName.PROCESS_PAYMENT),
    ),
    var created: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC),
    var updated: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC)
) {

    fun addPendingAction(action: Action?) {
        action?.let {
            pendingActions[action.name] = action
        }
    }

    fun isState(state: State): Boolean {
        return pendingActions.count { it.value.state == state } == pendingActions.size
    }
}
