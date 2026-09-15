package nel.marco

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestClient
import kotlin.system.exitProcess


@SpringBootApplication
class Main


fun main() {
    runApplication<Main>()
}


@Component
class CommandlineRunnerX : CommandLineRunner {

    override fun run(vararg args: String) {
        RestClient.builder().baseUrl("http://localhost:8080/v1/hello?page").build().get().retrieve()
            .body(String::class.java)

        exitProcess(0)
    }


}


@RestController
@RequestMapping("/v1")
class HelloWorld {

    @GetMapping(value = ["/hello"])
    fun getStaticPage(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) name1: String? = "name1",
        @RequestParam(required = true, defaultValue = "") name2: String = "name2", // BREAKS
        @RequestParam(required = false, defaultValue = "name3") name3: String = "XXXXXXX",
        @RequestParam(required = true, defaultValue = "") name4: String = "XXXXXXX", // BREAKS
    ): String {

        println("name $name")
        println("name1 $name1")
        println("name2 $name2")
        println("name3 $name3")
        println("name4 $name4")

        return "hello $name!"
    }

}