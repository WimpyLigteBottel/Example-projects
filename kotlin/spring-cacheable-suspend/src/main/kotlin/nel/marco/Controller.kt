package nel.marco

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import kotlin.random.Random

@RestController
class Controller(
    val slowLogic: SlowLogic
) {
    @GetMapping
    suspend fun helloWorld(@RequestParam(defaultValue = "0") number: String) = slowLogic.randomResponse(number, UUID.randomUUID().toString())
}


@Service
class SlowLogic {

    // 1. What will happen on the first run?
    // A: return result and print?
    // B: will it throw runtime exception?


    // (do the thing in pom file)


    // 2. What will happen on first run?
    // 3. What will happen on second run?

    @Cacheable(value = ["randomResponse"])
    suspend fun randomResponse(input: String, randomText: String): String {
        println("Hello i have been called -> $randomText")
        val random = Random.nextInt(0, 1000)
        return "$input -> $random"
    }
}
