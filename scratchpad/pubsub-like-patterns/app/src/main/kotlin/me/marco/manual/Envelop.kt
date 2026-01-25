package me.marco.manual

import java.util.*

data class Envelop(
    val id: UUID?,
    val message: String?,
    var state: EnvelopState
) {


    enum class EnvelopState {
        READY,
        INFLIGHT,
        COMPLETED
    }
}