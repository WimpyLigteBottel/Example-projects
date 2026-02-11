package me.marco.customer.service

import me.marco.customer.command.Command
import me.marco.customer.command.CustomerCommandHandler
import me.marco.customer.service.dto.CreateCustomerDto
import me.marco.customer.service.dto.Customer
import org.springframework.stereotype.Service
import java.util.*

@Service
open class CustomerService(
    private val commandHandler: CustomerCommandHandler
) {
    fun createCustomer(request: CreateCustomerDto): Customer {
        val command = Command.CreateCustomerCommand(
            UUID.randomUUID().toString(),
            request.name,
            request.age,
            request.email
        )
        commandHandler.handle(command)

        return commandHandler.getCustomer(command.customerId)
    }

    fun getCustomer(id: String): Customer {
        return commandHandler.getCustomer(id)
    }

    fun customerExist(id: String): Boolean {
        val customer = getCustomer(id)

        if (customer.version == 0L || customer.deleted || id == "")
            return false

        return true
    }

    fun deleteCustomer(id: String) {
        val command = Command.DeleteCustomerCommand(id)
        commandHandler.handle(command)
    }
}