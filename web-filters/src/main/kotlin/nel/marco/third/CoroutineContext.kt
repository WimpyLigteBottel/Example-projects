package nel.marco.third

import java.util.UUID
import java.util.concurrent.Executors.newFixedThreadPool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactor.asCoroutineContext
import nel.marco.second.CUSTOM_ID
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.core.Ordered.LOWEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono


@Order(HIGHEST_PRECEDENCE)
@Component
class CustomContextFilter : WebFilter {
    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain,
    ): Mono<Void> =
        chain.filter(exchange).contextWrite { ctx->
            println("CUSTOM_ID has been added")
            ctx.put(CUSTOM_ID,  UUID.randomUUID().toString())
        }
}

@Component
@Order(HIGHEST_PRECEDENCE + 1)
class CoroutineCustomWebFilter : WebFilter {
    private val closeableCoroutineDispatcher = newFixedThreadPool(10).asCoroutineDispatcher()

    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain,
    ): Mono<Void> =
        Mono
            .deferContextual { ctx ->
                chain.filter(exchange).doOnTerminate {
                    // done AT THE END of the reactor chain execution
                    val requestScopeContext = ctx.getOrEmpty<RequestScopeCoroutineContext>(REQUEST_SCOPE_CONTEXT_KEY)
                    if (requestScopeContext.isPresent) {
                        // only launch a coroutine if we know we have to
                        CoroutineScope(ctx.asCoroutineContext() + closeableCoroutineDispatcher).launch {
                            //execute task
                        }
                    }
                }.contextWrite { ctx ->

                    println("Cotext passed on")
                    // done AT THE START of the reactor chain execution
                    ctx.put(REQUEST_SCOPE_CONTEXT_KEY, RequestScopeCoroutineContext(exchange))
                }
            }
}
