package nel.marco.aspect

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.ReactorContext
import kotlinx.coroutines.runBlocking
import nel.marco.third.CustomContext
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component
import kotlin.coroutines.coroutineContext

@Aspect
//@Component
class HelloWorldAspect {

    @Before("@within(org.springframework.web.bind.annotation.RestController)")
     fun helloWorld() {
        println("HELLO THIS IS THE ASPECT")

        runBlocking {
            someCoroutineHandler()
        }

    }

    suspend fun someCoroutineHandler() {
        val customContext = coroutineContext[ReactorContext]?.context?.get<CustomContext>(CustomContext)
        println("2nd. Accessed CUSTOM_ID in Coroutine: ${customContext?.getCustomId()}")
        breakingContext()
    }

    fun breakingContext() {
        runBlocking(Dispatchers.IO) {
            val customContext = coroutineContext[CustomContext.Key]
            println("3rd. Accessed CUSTOM_ID in broken context: ${customContext?.getCustomId()}")
        }
    }
}