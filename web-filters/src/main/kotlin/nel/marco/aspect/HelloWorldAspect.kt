package nel.marco.aspect

import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.ReactorContext
import kotlinx.coroutines.reactor.asCoroutineContext
import kotlinx.coroutines.runBlocking
import nel.marco.second.CUSTOM_ID
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.util.context.ContextView

@Aspect
@Component
class HelloWorldAspect {

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    fun helloWorld(pjp: ProceedingJoinPoint): Mono<Any> {
        println("HELLO THIS IS THE ASPECT")

        return Mono.deferContextual { ctx ->
//            withoutRunBlocking(ctx)
//            runBlocking(ctx)

            pjp.proceed() as Mono<out Any>?
        }
    }

    fun withoutRunBlocking(ctx: ContextView) {
        val ans = ctx.getNotCoroutineId()

        println("2nd. Accessed CUSTOM_ID in HelloWorldAspect.deferContextual: ${ans}")
    }

    fun runBlocking(ctx: ContextView) {
        //Remember gotcha in here
        runBlocking(Dispatchers.IO) {
            val ans = getId()

            println("2nd. Accessed CUSTOM_ID in HelloWorldAspect.deferContextual: ${ans}")
        }

    }

}

fun ContextView.getNotCoroutineId() = this.getOrEmpty<String>(CUSTOM_ID)
suspend fun getId() = coroutineContext[ReactorContext]?.context?.getOrEmpty<String>(CUSTOM_ID)