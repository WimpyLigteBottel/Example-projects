package me.marco.function4

typealias `📜` = String
typealias `📦` = List<*>
typealias `🔢` = Int
typealias `💼` = () -> Unit

fun `🐒🙈🙉🙊`()
fun `🔈🌍`(`✉️`: `📜`) {
    println(`✉️`)
}

fun `🔁`(`🔢`: `🔢`, `💼`: `💼`) = repeat(`🔢`) {
    `💼`()
}

fun `🔁🔈🌍`(`✉️`: `📜`, `🔢`: `🔢`) {
    `🔁`(`🔢`) {
        `🔈🌍`(`✉️`)
    }
}

fun main() {
    `🔁🔈🌍`("Hello people", 5)
}

