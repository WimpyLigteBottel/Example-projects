package nel.marco.third

import java.io.Closeable
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass
import org.slf4j.LoggerFactory
import org.springframework.web.server.ServerWebExchange

val REQUEST_SCOPE_CONTEXT_KEY = RequestScopeCoroutineContext::class.java

val LOG = LoggerFactory.getLogger("CoroutineContextLoggers")

interface ExchangeClosable {
    suspend fun close(exchange: ServerWebExchange)
}

interface SuspendClosable {
    suspend fun close()
}


interface RequestScopeContext {
    val exchange: ServerWebExchange

    val isNotEmpty: Boolean
    operator fun <T : Any> get(klass: KClass<T>, setter: () -> T): T
    operator fun <T : Any> get(klass: KClass<T>): T?
    operator fun <T : Any> set(klass: KClass<T>, value: T)
}


class RequestScopeCoroutineContext(override val exchange: ServerWebExchange) : RequestScopeContext, SuspendClosable, AbstractCoroutineContextElement(KEY) {
    companion object KEY : CoroutineContext.Key<RequestScopeCoroutineContext>

    private val vals: ConcurrentHashMap<Any, Any> = ConcurrentHashMap<Any, Any>()
    override val isNotEmpty: Boolean
        get() = vals.values.any { it is ExchangeClosable || it is SuspendClosable || it is Closeable }

    override fun <T : Any> get(klass: KClass<T>, setter: () -> T): T {
        @Suppress("UNCHECKED_CAST")
        return vals.computeIfAbsent(klass) { setter() } as T
    }

    override fun <T : Any> get(klass: KClass<T>): T? {
        @Suppress("UNCHECKED_CAST")
        return vals[klass] as T?
    }

    override operator fun <T : Any> set(klass: KClass<T>, value: T) {
        vals.putIfAbsent(klass, value)
    }

    override suspend fun close() {
        vals.values.forEach {
            kotlin.runCatching {
                when (it) {
                    is ExchangeClosable -> it.close(exchange)
                    is SuspendClosable -> it.close()
                    is Closeable -> it.close()
                }
            }.onFailure {
                LOG.debug("RequestScopeCoroutineContext.close().err", it)
            }
        }
    }
}