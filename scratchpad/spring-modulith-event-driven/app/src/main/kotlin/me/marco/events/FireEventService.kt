package me.marco.events

import me.marco.order.Order
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity

@Service
class FireEventService {

    val logger = LoggerFactory.getLogger(this::class.java)

    val restClient = RestClient.create("http://localhost:8080/")


    fun fireEvent(event: Event) {
        logger.info("{}", event)

        when (event) {
            is CreateOrderEvent -> {
                val r = getOrder(event.orderID)
            }

            is UpdateOrderEvent -> {
                val r = restClient
                    .post()
                    .uri("/orders/${event.orderID}")
                    .body(event.order)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity<Order>()
            }

            is AddItemEvent -> {
                val order =  getOrder(event.orderID)
                val newOrder = order.copy(order.id, items = order.items + event.orderItemId, order.paid)
                fireEvent(UpdateOrderEvent(newOrder.id, order))
            }

            is PaidOrderEvent -> {
                val order =  getOrder(event.orderID)
                val newOrder = order.copy(order.id, items = order.items, order.paid)
                fireEvent(UpdateOrderEvent(newOrder.id, order))
            }
        }
    }

    fun getOrder(orderId: String): Order {
        val r = restClient.get()
            .uri("/orders/$orderId")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity<Order>()

        when {
            !r.statusCode.is2xxSuccessful -> throw RuntimeException("Invalid status for $orderId order")
            r.body == null ->   throw RuntimeException("Order is empty $orderId")
        }

        return r.body!!
    }
}