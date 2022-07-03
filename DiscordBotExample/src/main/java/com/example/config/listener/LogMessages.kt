package com.example.config.listener

import com.example.config.command.Command
import com.example.config.util.LogUtil
import com.example.dto.CustomMessage
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LogMessages constructor(var discordApi: DiscordApi, private val logUtil: LogUtil) : Command {

    companion object {
        var log: Logger = LoggerFactory.getLogger(LogMessages.javaClass)
    }

    /**
     * Will be logging all the message that is happening on the server
     */
    fun logMessage() {
        discordApi.addMessageCreateListener { event: MessageCreateEvent ->
            val message = event.message
            if (message.content.isNotBlank()) {
                log.info("Something said [message={}]", logUtil.toJson(CustomMessage(message)))
            }
        }
    }

    fun scrapMessages() {
        discordApi.servers.forEach {
            if (it.name == "Testing server") {
                it.textChannels.forEach { channel ->
                    run {
                        channel.asServerTextChannel().get().getMessages(100).get()
                            .forEach { message ->
                                log.info("Channel={};Messages={}", channel.name, logUtil.toJson(CustomMessage(message)))
                            }
                    }

                }
            }
        }
    }

    override fun command(): Boolean {
        logMessage()
        scrapMessages()
        return true;
    }
}

