package com.example.dto

import org.javacord.api.entity.message.Message

class CustomMessage(message: Message) {

    var name: String = ""
    var server: String = ""
    var content: String = ""

    init {
        name = getUser(message)
        server = getServer(message)
        content = message.content
    }

    private fun getUser(message: Message): String {
        val userAuthor = message.userAuthor
        if (userAuthor.isEmpty)
            return "???"

        return userAuthor.get().name
    }

    private fun getServer(message: Message): String {
        val server = message.server
        if (server.isEmpty)
            return "???"

        return server.get().name
    }


}
