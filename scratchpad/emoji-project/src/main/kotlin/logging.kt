package me.marco.function5

import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("logging")


fun main() {
    `ℹ️`("info message")
    `⚠️`("Warning message")
    `‼️`("error message")
}



fun `⚠️`(message: String, throwable: Throwable? = null) {
    logger.warn(message, throwable)
}

fun `‼️`(message: String, throwable: Throwable? = null) {
    logger.error(message, throwable)
}

fun `ℹ️`(message: String, throwable: Throwable? = null) {
    logger.info(message, throwable)
}
