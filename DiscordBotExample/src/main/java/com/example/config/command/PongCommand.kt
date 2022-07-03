package com.example.config.command

import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent
import org.javacord.api.interaction.SlashCommand
import org.javacord.api.interaction.SlashCommandInteraction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PongCommand constructor(var discordApi: DiscordApi) : Command {

    companion object {
        var log: Logger = LoggerFactory.getLogger(PongCommand.javaClass)
    }


    override fun command(): Boolean {
        log.debug("Command has been executed [className={}]", this.javaClass.name)

        val pong = SlashCommand.with("pong", "Checks the functionality of this command")
            .createGlobal(discordApi)
            .join()

        discordApi.addSlashCommandCreateListener { event ->
            val slashCommandInteraction: SlashCommandInteraction = event.slashCommandInteraction

            if (slashCommandInteraction.commandId == pong.id) {
                log.info("ping")
            }
        }

        discordApi.addMessageCreateListener { event: MessageCreateEvent ->
            if (event.message.content == "!ping"){
                log.info("pong")
                event.channel.sendMessage("pong!")
            }
        }

        return true;
    }
}