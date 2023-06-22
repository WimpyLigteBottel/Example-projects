package org.example.service

import org.example.dto.ActionAndState
import org.example.dto.State
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class OrderPaymentService(
    private val webClient: WebClient = WebClient.create(),
    @Value("\${order-server-url}") private val orderServerUrl: String,
    @Value("\${payment-server-url}") private val paymentServerUrl: String
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun createOrder(id: String): ActionAndState {

        try {
            val response =
                webClient.get()
                    .uri("$orderServerUrl/create?id=$id")
                    .retrieve()
                    .bodyToMono(ActionAndState::class.java)
                    .block()

            return response!!
        } catch (e: Exception) {
            log.info("failed to create order[id=$id]")
            return ActionAndState(name = "orderCreation", state = State.FAILED)
        }
    }


    fun createPayment(id: String): ActionAndState {

        try {
            val response =
                webClient.get()
                    .uri("$paymentServerUrl/create?id=$id")
                    .retrieve()
                    .bodyToMono(ActionAndState::class.java)
                    .block()

            return response!!
        } catch (e: Exception) {
            log.info("failed to create payment[id=$id]")
            return ActionAndState(name = "paymentProcessing", state = State.FAILED)
        }
    }

    fun stopProcessing(actionAndState: ActionAndState): ActionAndState {

        if (actionAndState.name == "orderCreation") {
            return webClient.get()
                .uri("$orderServerUrl/rollback?id=${actionAndState.id}")
                .retrieve()
                .bodyToMono(ActionAndState::class.java)
                .block()!!
        }

        if (actionAndState.name == "paymentProcessing") {
            return webClient.get()
                .uri("$paymentServerUrl/rollback?id=${actionAndState.id}")
                .retrieve()
                .bodyToMono(ActionAndState::class.java)
                .block()!!
        }



        throw RuntimeException("Failed to process the order")
    }

}