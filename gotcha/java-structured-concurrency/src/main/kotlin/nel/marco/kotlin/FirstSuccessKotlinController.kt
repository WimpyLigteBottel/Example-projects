package nel.marco.kotlin

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.supervisorScope
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.io.IOException
import kotlin.random.Random

@RestController
class FirstSuccessKotlinController {
    @GetMapping("/kotlin/partial")
    suspend fun getFirstSuccessResponse(): String = supervisorScope {
        val slowButReliable = async { fetch("slow response", 1000) }
        val fastButFlaky = async { fetch("fast response", 200, isFlaky = true) }


        runCatching { fastButFlaky.await() }.onSuccess {
            return@supervisorScope it
        }

        runCatching { slowButReliable.await() }.onSuccess {
            return@supervisorScope it
        }

        "Both Failed"
    }

    suspend fun fetch(info: String, delayWait: Long = 500, isFlaky: Boolean = false): String {
        delay(delayWait)
        if (isFlaky && Random.Default.nextInt(0, 100) > 50) throw IOException("Network flaky")
        return info
    }
}