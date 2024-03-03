package nel.marco

import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@SpringBootApplication
class Main

fun main(args: Array<String>) {
    runApplication<Main>(*args);
}


@Configuration
@ConfigurationProperties(prefix = "custom")
class MarcoProperties {

    @PostConstruct
    fun doesHelloMessageContainHello() {
        if (!helloMessage.contains("hello", true)) {
            throw InvalidHelloMessageException("Does not contain valid hello message")
        }
    }


    open lateinit var helloMessage: String
}


// Easy way to use the string
@RestController
class HelloWorld(val marcoProperties: MarcoProperties) {

    @GetMapping
    fun getHelloWorld() = marcoProperties.helloMessage

}