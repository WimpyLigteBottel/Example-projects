package nel.marco.aspect

import nel.marco.second.CUSTOM_ID
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Aspect
@Component
class HelloWorldAspect {

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    fun helloWorld(pjp: ProceedingJoinPoint): Mono<Any> {
        println("HELLO THIS IS THE ASPECT")

        return Mono.deferContextual { ctx ->
            println("2nd. Accessed CUSTOM_ID in HelloWorldAspect.deferContextual: ${ctx.get<String>(CUSTOM_ID)}")
            pjp.proceed() as Mono<out Any>?
        }
    }

}