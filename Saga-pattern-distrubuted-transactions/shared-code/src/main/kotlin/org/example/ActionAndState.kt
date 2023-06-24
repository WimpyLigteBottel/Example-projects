package org.example

data class ActionAndState(
    var globalId: String,
    var name: PendingActionName = PendingActionName.UNKNOWN,
    var internalId: String? = null,
    var state: State = State.PENDING,
) {

    //needed to deserialize
    constructor() : this("")


    fun pending(): ActionAndState {
        return this.copy(
            state = State.PENDING
        )
    }


    fun failed(): ActionAndState {
        return this.copy(
            state = State.FAILED
        )
    }

    fun rollback(): ActionAndState {
        return this.copy(
            state = State.ROLLBACK
        )
    }
}

enum class State {
    PENDING,
    SUCCESS,
    ROLLBACK,
    FAILED
}


enum class PendingActionName {
    CREATE_ORDER,
    PROCESS_PAYMENT,
    UNKNOWN
}