package com.example.config.command

import kotlinx.coroutines.runBlocking
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.Message
import org.javacord.api.event.message.MessageCreateEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ClearMessageCommand constructor(var discordApi: DiscordApi) : Command {

    companion object {
        var log: Logger = LoggerFactory.getLogger(PongCommand.javaClass)
    }


    override fun command(): Boolean {
        log.debug("Command has been executed [className={}]", this.javaClass.name)

        discordApi.addMessageCreateListener { event: MessageCreateEvent ->
            if (event.message.content == "!clear")
                clearAllText(event.message)
            if (event.message.content == "!clear bots")
                clearBotText(event.message)
        }

        return true;
    }


    fun clearAllText(message: Message) {
        val messages = message.channel.getMessages(1000).get()
        runBlocking {
            message.channel.bulkDelete(messages)
        }
        log.info("Done delete message in channel [name={}]", message.serverTextChannel.get().name)
    }

    fun clearBotText(message: Message) {
        val messages = message.channel.getMessages(1000).get()

        val messagesToDelete = messages.filter {
            it.userAuthor.get().isBot
        }
        runBlocking {
            message.channel.bulkDelete(messagesToDelete)
        }
        log.info("Done delete message in channel [name={}]", message.serverTextChannel.get().name)
    }

}