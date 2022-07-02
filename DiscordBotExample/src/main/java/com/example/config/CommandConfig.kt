package com.example.config

import com.example.config.command.PongCommand
import org.javacord.api.DiscordApi
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class CommandConfig constructor(private val discordApi: DiscordApi) {

    @PostConstruct
    fun setup() {
        PongCommand(discordApi).command()
    }

}