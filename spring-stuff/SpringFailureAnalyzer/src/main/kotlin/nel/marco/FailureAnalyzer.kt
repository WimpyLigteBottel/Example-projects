package nel.marco

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer
import org.springframework.boot.diagnostics.FailureAnalysis


/**
 * Use this exception to indicate that the hello message is wrong
 */
class InvalidHelloMessageException(message: String) : RuntimeException(message)


class FailureAnalyzer : AbstractFailureAnalyzer<InvalidHelloMessageException>() {
    override fun analyze(rootFailure: Throwable, cause: InvalidHelloMessageException): FailureAnalysis {
        val description = """You might have invalid properties configured. 
            |Exception message:  `${cause.message}`""".trimMargin()

        val action = """
                1. Your message should contain 'Hello'
                """.trimIndent()

        return FailureAnalysis(description, action, cause)
    }
}