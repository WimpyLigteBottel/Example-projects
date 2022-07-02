package com.example.config.listener

import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LogMessages constructor(var discordApi: DiscordApi) {

    companion object {
        var log: Logger = LoggerFactory.getLogger(LogMessages.javaClass)
    }


    /**
     * Will be logging all the message that is happening on the server
     */
    fun logMessage() {
        discordApi.addMessageCreateListener { event: MessageCreateEvent ->
            if (event.message.content.isNotBlank())
                log.info(event.message.content)
        }
    }

}
