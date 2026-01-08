package me.marco.orderprocessing.service

import me.marco.common.Order
import me.marco.orderprocessing.OrderCommandHandler
import me.marco.orderprocessing.models.Command.*
import me.marco.orderprocessing.service.dto.AddItemRequestDTO
import me.marco.orderprocessing.service.dto.CreateOrderRequestDTO
import me.marco.orderprocessing.service.dto.MarkAsPaidRequestDTO
import org.springframework.stereotype.Service
import java.util.*


@Service
class OrderService(
    private val commandHandler: OrderCommandHandler
) {

    fun getOrder(orderId: String): Order {
        return commandHandler.getOrder(orderId)
    }

    fun createOrder(request: CreateOrderRequestDTO): String {
        val command = CreateOrderCommand(aggregateId = request.orderId ?: UUID.randomUUID().toString())


        return commandHandler.handle(command).getOrThrow().aggregateId
    }

    fun addItem(orderId: String, request: AddItemRequestDTO): String {
        val command = AddItemCommand(
            aggregateId = orderId,
            itemId = request.itemId,
            name = request.name,
            price = request.price,
            quantity = request.quantity,
        )

        return commandHandler.handle(command).getOrThrow().aggregateId
    }

    fun markAsPaid(orderId: String, request: MarkAsPaidRequestDTO): String {
        val command = MarkOrderAsPaidCommand(
            aggregateId = orderId,
            paymentMethod = request.paymentMethod,
        )

        return commandHandler.handle(command).getOrThrow().aggregateId
    }

    fun deleteOrder(orderId: String): String {
        val command = DeleteOrderCommand(orderId)

        return commandHandler.handle(command).getOrThrow().aggregateId
    }

}
