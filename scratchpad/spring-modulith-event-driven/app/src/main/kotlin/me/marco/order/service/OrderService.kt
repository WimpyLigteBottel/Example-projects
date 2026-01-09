package me.marco.order.service

import me.marco.order.command.Command
import me.marco.order.command.OrderCommandHandler
import me.marco.order.service.dto.AddItemRequestDTO
import me.marco.order.service.dto.CreateOrderRequestDTO
import me.marco.order.service.dto.MarkAsPaidRequestDTO
import me.marco.order.service.dto.Order
import org.springframework.stereotype.Service
import java.util.*


@Service
class OrderService(
    private val commandHandler: OrderCommandHandler
) {

    fun getOrder(orderId: String): Order {
        return commandHandler.getOrder(orderId)
    }

    fun createOrder(request: CreateOrderRequestDTO): Order {
        val command =
            Command.CreateOrderCommand(aggregateId = UUID.randomUUID().toString(), customerId = request.customerId)

        commandHandler.handle(command)

        return commandHandler.getOrder(command.aggregateId)
    }

    fun addItem(orderId: String, request: AddItemRequestDTO): Order {
        val command = Command.AddItemCommand(
            aggregateId = orderId,
            itemId = request.itemId,
            name = request.name,
            price = request.price,
            quantity = request.quantity,
        )

        commandHandler.handle(command)
        return commandHandler.getOrder(command.aggregateId)
    }

    fun markAsPaid(orderId: String, request: MarkAsPaidRequestDTO): Order {
        val command = Command.MarkOrderAsPaidCommand(
            aggregateId = orderId,
            paymentMethod = request.paymentMethod,
        )

        commandHandler.handle(command)
        return commandHandler.getOrder(command.aggregateId)
    }

    fun deleteOrder(orderId: String): Order {
        val command = Command.DeleteOrderCommand(orderId)

        commandHandler.handle(command)
        return commandHandler.getOrder(command.aggregateId)
    }

}
