package org.example.service

import org.example.dto.ActionAndState
import org.example.dto.State
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class OrderPaymentService(
    @Value("\${order-server-url}") private val orderServerUrl: String,
    @Value("\${payment-server-url}") private val paymentServerUrl: String
) {

    private val webClient: WebClient = WebClient.create()

    private val log = LoggerFactory.getLogger(this::class.java)

    fun createOrder(id: String): ActionAndState {
        try {
            val response =
                webClient.get()
                    .uri("$orderServerUrl/create?id=$id&name=orderCreation")
                    .retrieve()
                    .bodyToMono(ActionAndState::class.java)
                    .block()

            return response!!
        } catch (e: Exception) {
            log.error("failed to create order[id=$id]", e)
            return ActionAndState(name = "orderCreation", state = State.FAILED)
        }
    }


    fun createPayment(id: String): ActionAndState {

        try {
            val response =
                webClient.get()
                    .uri("$paymentServerUrl/create?id=$id&name=paymentProcessing")
                    .retrieve()
                    .bodyToMono(ActionAndState::class.java)
                    .block()

            return response!!
        } catch (e: Exception) {
            log.error("failed to create payment[id=$id]", e)
            return ActionAndState(name = "paymentProcessing", state = State.FAILED)
        }
    }

    fun stopProcessing(actionAndState: ActionAndState): ActionAndState? {

        if (actionAndState.name == "orderCreation") {
            val block = webClient.post()
                .uri("$orderServerUrl/rollback")
                .bodyValue(actionAndState)
                .retrieve()
                .bodyToMono(ActionAndState::class.java)
                .block()

            return block
        }

        if (actionAndState.name == "paymentProcessing") {
            val block = webClient.post()
                .uri("$paymentServerUrl/rollback")
                .bodyValue(actionAndState)
                .retrieve()
                .bodyToMono(ActionAndState::class.java)
                .block()

            return block
        }



        throw RuntimeException("Failed to process the order")
    }

}