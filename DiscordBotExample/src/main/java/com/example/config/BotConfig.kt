package com.example.config

import com.example.config.command.Command
import com.example.config.command.PongCommand
import com.example.config.listener.LogMessages
import com.example.config.util.LogUtil
import org.javacord.api.DiscordApi
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class BotConfig constructor(private val discordApi: DiscordApi, private val logUtil: LogUtil) {

    companion object {
        var log: Logger = LoggerFactory.getLogger(BotConfig.javaClass)
        var counter = 0;
    }


    @PostConstruct
    fun setup() {
        log.info("Starting up config loading")
        incrementCounter(PongCommand(discordApi));
        incrementCounter(LogMessages(discordApi, logUtil));
        log.info("Finished loading config [counter={}]", counter)
    }


    private fun incrementCounter(command: Command) {
        try {
            command.command()
            counter++;
        } catch (e: Exception) {
            log.error("failed to execute command ", e)
        }

    }

}