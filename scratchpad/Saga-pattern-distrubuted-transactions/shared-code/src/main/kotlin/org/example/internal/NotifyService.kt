package org.example.internal

import org.example.api.Action
import org.springframework.resilience.annotation.Retryable
import org.springframework.web.client.RestClient

open class NotifyService(
    mainServerUrl: String
) {

    private val restClient: RestClient = RestClient.create(mainServerUrl)


    @Retryable(maxRetries = 3, delay = 1000)
    open fun notifyMainServer(action: Action) {

        Thread.sleep(100)
        restClient.post()
            .uri("/listen")
            .body(action)
            .retrieve()
            .toBodilessEntity()
    }
}