package nel.marco

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.client.WebClient


@SpringBootApplication
class Main


fun main() {
    runApplication<Main>()
}


@Component
class CommandlineRunnerX : CommandLineRunner {
    override fun run(vararg args: String?) {

        WebClient.builder().baseUrl("http://localhost:8080/v1/hello?page").build().get().retrieve().bodyToMono(String::class.java)
            .subscribe()
    }


}


@RestController
@RequestMapping("/v1")
@CrossOrigin
class HelloWorld {

    @GetMapping(value = ["/hello"])
    fun getStaticPage(
        @RequestParam(required = false) name: String? = null,
        @RequestParam page: String = "1",
        @RequestParam size: String? = "10",
        @RequestParam(defaultValue = "3name") name3: String = "name3",
    ): String {

        println("name $name")
        println("name1 $name1")
        println("name2 $name2")
        println("name3 $name3")

        return "hello $name!"
    }

}