package org.example.service

import org.example.api.ActionAndState
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service

class NotifyMainServerService(
    @Value("\${main-server-url}")
    private val mainServerUrl: String
) {

    private val webClient: WebClient = WebClient.create()

    private val log = LoggerFactory.getLogger(this::class.java)

    fun respond(id: String, actionAndState: ActionAndState) {

        webClient.post()
            .uri("$mainServerUrl/listen?id=$id")
            .bodyValue(actionAndState)
            .retrieve()
            .toBodilessEntity()
            .block()


    }


}