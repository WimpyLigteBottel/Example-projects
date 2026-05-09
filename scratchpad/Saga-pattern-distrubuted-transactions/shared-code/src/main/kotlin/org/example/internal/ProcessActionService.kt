package org.example.internal

import org.example.api.Action
import org.example.api.PendingActionName
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.web.client.RestClient

open class ProcessActionService(
    private val baseUrl: String,
    private val name: PendingActionName = PendingActionName.UNKNOWN
) {

    private val webClient: RestClient = RestClient.builder()
        .baseUrl(baseUrl)
        .build()
    private val log = LoggerFactory.getLogger(this::class.java)


    /**
     * Starts the process of orderCreation, this is fire and forget operation

     */
    @Async
    fun createFireAndForget(id: String) {
        val action = Action(id, name)

        try {
            webClient.post()
                .uri("/create")
                .body(action)
                .retrieve()
                .body(Action::class.java)

        } catch (e: Exception) {
            log.error("failed to create [id=$id]", e)
        }
    }

    /**
     * This is blocking and waits till we get response to verify that it has rolled back
     */
    fun stopProcessingBlocking(action: Action): Boolean {
        val isSuccess = runCatching {
            webClient.post()
                .uri("$baseUrl/rollback")
                .body(action)
                .retrieve()
                .body(Action::class.java)

        }
        return isSuccess.isSuccess

    }

}