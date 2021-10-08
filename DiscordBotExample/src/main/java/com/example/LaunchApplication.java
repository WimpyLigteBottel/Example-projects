package com.example;

import lombok.extern.slf4j.Slf4j;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Slf4j
public class LaunchApplication {

    public static void main(String[] args) {
        SpringApplication.run(LaunchApplication.class, args);
        log.info("Started application");
    }
}

@Configuration
@Slf4j
class Config {

    @Bean
    public DiscordApi discordApi() {
        String token = "MzEzNzM0MTE4Mzg5MDU1NDkw.WRnpqg.Iqd7UVbFf7y8cm9TJnlPQ6LWhtE";

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        // Print the invite url of your bot
        System.out.println(
                "You can invite the bot by using the following url: " + api.createBotInvite());

        setupCommands(api);
        return api;
    }

    public void setupCommands(DiscordApi discordApi) {
        // Add a listener which answers with "Pong!" if someone writes "!ping"
        discordApi.addMessageCreateListener(
                event -> {
//                    if (event.getChannel() instanceof PrivateChannel s) {
//                        User user = s.getRecipient().get();
//                        if (user.getName().equalsIgnoreCase("Wimpy Ligte Bottel")) {
//
//                            boolean isBotMessage = event.getMessage().getAuthor().isBotUser();
//                            if (!isBotMessage)
//                                event.getChannel().sendMessage("pong!");
//                        }
//
//                    }

                });

        SlashCommand.with("ping", "A simple ping pong command!").createGlobal(discordApi).join();

        discordApi.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
            if (slashCommandInteraction.getCommandName().equals("ping")) {
                slashCommandInteraction.createImmediateResponder()
                        .setContent("Pong!")
                        .setFlags(MessageFlag.EPHEMERAL) // Only visible for the user which invoked the command
                        .respond();
            }
        });
    }
}

