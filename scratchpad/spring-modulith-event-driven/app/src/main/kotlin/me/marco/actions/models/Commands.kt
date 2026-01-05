package me.marco.actions.models

sealed interface Command {
    val aggregateId: String

    data class CreateOrderCommand(
        override val aggregateId: String
    ) : Command

    data class ClearOrderCommand(
        override val aggregateId: String
    ) : Command

    data class AddItemCommand(
        override val aggregateId: String,
        val itemId: String,
        val name: String,
        val price: Double,
        val quantity: Int
    ) : Command

    data class MarkOrderAsPaidCommand(
        override val aggregateId: String,
        val paymentMethod: String
    ) : Command

}

