package nel.marco.singletons

import jakarta.inject.Singleton

@Singleton
class HelloWorld {

    fun greet() {
        println("Hello")
    }
}