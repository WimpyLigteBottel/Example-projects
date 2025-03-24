package org.example

data class Action(
    var globalId: String,
    var name: PendingActionName = PendingActionName.UNKNOWN,
    var internalId: String? = null,
    var state: State = State.PENDING,
) {

    //needed to deserialize
    constructor() : this("")


    fun pending(): Action = this.copy(
        state = State.PENDING
    )


    fun failed(): Action = this.copy(
        state = State.FAILED
    )

    fun rollback(): Action = this.copy(
        state = State.ROLLBACK
    )
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