package me.marco.customer.command

sealed interface Command {
    val customerId: String

    data class CreateCustomerCommand(
        override val customerId: String,
        val name: String,
        val age: Int,
        val email: String
    ) : Command

    data class DeleteCustomerCommand(
        override val customerId: String,
    ) : Command
}
