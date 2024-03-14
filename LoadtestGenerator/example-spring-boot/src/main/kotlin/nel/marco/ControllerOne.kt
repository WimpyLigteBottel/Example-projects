package nel.marco

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.util.concurrent.ThreadLocalRandom


data class Person(
    var name: String? = null,
    var id: String? = null
)

@RestController
@RequestMapping("/v1")
class ControllerOne {

    val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/hello/{id}")
    fun getHello(@PathVariable id: String): String {
        sleepBetween(10, 30)

        if(ThreadLocalRandom.current().nextLong(100) < 2){
            throw RuntimeException("I SHOULD FAIL")
        }
        return "GET DONE!"
    }


    @PostMapping("/hello")
    fun postHello(@RequestBody person: Person): String {
        sleepBetween(50, 70)
        return "POST DONE!"
    }


    @PutMapping("/hello")
    fun putHello(@RequestBody person: Person): String {
        sleepBetween(50, 70)
        return "PUT DONE!"
    }

    @DeleteMapping("/hello/{id}")
    fun deleteHello(@PathVariable id: String): String {
        sleepBetween(50, 70)
        return "DELETE DONE!"
    }

    private fun sleepBetween(min: Long, max: Long) {
        val sleepTime = ThreadLocalRandom.current().nextLong(min, max)

        try {
            Thread.sleep(sleepTime)
        } catch (e: Exception) {
            log.error("Failed!", e)
        }
    }

}


