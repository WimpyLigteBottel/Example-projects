package com.example

import org.javacord.api.DiscordApi
import org.javacord.api.DiscordApiBuilder
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

        log.info("You can invite the bot by using the following [url={}] ", api.createBotInvite())

        return api
    }


}