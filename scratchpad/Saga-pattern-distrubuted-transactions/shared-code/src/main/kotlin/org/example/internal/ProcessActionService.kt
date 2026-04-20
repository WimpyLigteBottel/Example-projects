package org.example.internal

import org.example.api.Action
import org.example.api.PendingActionName
import org.slf4j.LoggerFactory
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
    fun createFireAndForget(id: String): Action {
        val action = Action(id, name)

        try {
            val response = webClient.post()
                .uri("/create")
                .body(action)
                .retrieve()
                .body(Action::class.java)

            return action.pending()
        } catch (e: Exception) {
            log.error("failed to create [id=$id]", e)
        }

        return action.failed()
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