package com.example.config.util

import com.google.gson.Gson
import org.javacord.api.entity.message.Message
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class LogUtil {


    companion object {
        var log: Logger = LoggerFactory.getLogger(this.javaClass)
        var gson = Gson()
    }


    fun toJson(value: Any): String {
        return gson.toJson(value)
    }
}