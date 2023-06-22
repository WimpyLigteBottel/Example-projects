package org.example.service

import org.example.dto.ActionAndState
import org.example.dto.State
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.time.Duration
import java.util.concurrent.TimeUnit

@Service
class OrderPaymentService(
    @Value("\${order-server-url}") private val orderServerUrl: String,
    @Value("\${payment-server-url}") private val paymentServerUrl: String
) {

    private val webClient: WebClient = WebClient.create()

    private val log = LoggerFactory.getLogger(this::class.java)

    fun createOrder(id: String): ActionAndState {
        try {
            webClient.get()
                .uri("$orderServerUrl/create?id=$id&name=orderCreation")
                .retrieve()
                .bodyToMono(Void::class.java)
                .subscribe()

            return ActionAndState(name = "orderCreation", state = State.PENDING)
        } catch (e: Exception) {
            log.error("failed to create order[id=$id]", e)
            return ActionAndState(name = "orderCreation", state = State.FAILED)
        }
    }


    fun createPayment(id: String): ActionAndState {

        try {
            webClient.get()
                .uri("$paymentServerUrl/create?id=$id&name=paymentProcessing")
                .retrieve()
                .bodyToMono(Void::class.java)
                .subscribe()


            return ActionAndState(name = "paymentProcessing", state = State.PENDING)
        } catch (e: Exception) {
            log.error("failed to create payment[id=$id]", e)
            return ActionAndState(name = "paymentProcessing", state = State.FAILED)
        }
    }

    fun stopProcessing(id: String, actionAndState: ActionAndState) {

        if (actionAndState.name == "orderCreation") {
             webClient.post()
                .uri("$orderServerUrl/rollback?id=$id")
                .bodyValue(actionAndState)
                .retrieve()
                 .bodyToMono(Void::class.java)
                 .subscribe()

        }

        if (actionAndState.name == "paymentProcessing") {
            webClient.post()
                .uri("$paymentServerUrl/rollback?id=$id")
                .bodyValue(actionAndState)
                .retrieve()
                .bodyToMono(Void::class.java)
                .subscribe()

        }


    }

}