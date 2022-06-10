package com.example

import org.javacord.api.DiscordApi
import org.javacord.api.DiscordApiBuilder
import org.javacord.api.event.interaction.SlashCommandCreateEvent
import org.javacord.api.event.message.MessageCreateEvent
import org.javacord.api.interaction.SlashCommand
import org.javacord.api.interaction.callback.InteractionCallbackDataFlag
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
open class DiscordConfig {

    companion object {
        var log: Logger = LoggerFactory.getLogger(DiscordConfig.javaClass)
    }

    @Value("\${discord.token}")
    var token: String = ""


    @Bean
    open fun discordApi(): DiscordApi {
        var api = DiscordApiBuilder()
            .setWaitForServersOnStartup(true)
            .setToken(token)
            .login()
            .get(30, TimeUnit.SECONDS)


        // Print the invite url of your bot
        log.info("You can invite the bot by using the following url: " + api.createBotInvite())
        setupCommands(api)

        return api
    }

    fun setupCommands(discordApi: DiscordApi) {
        // Add a listener which answers with "Pong!" if someone writes "!ping"
        discordApi.addMessageCreateListener { event: MessageCreateEvent? -> }
        SlashCommand.with("ping", "A simple ping pong command!").createGlobal(discordApi).join()
        discordApi.addSlashCommandCreateListener { event: SlashCommandCreateEvent ->
            var slashCommandInteraction = event.slashCommandInteraction
            if (slashCommandInteraction.commandName == "ping") {
                slashCommandInteraction.createImmediateResponder()
                    .setContent("Pong!")
                    .setFlags(InteractionCallbackDataFlag.EPHEMERAL) // Only visible for the user which invoked the command
                    .respond()
            }
        }
    }
}