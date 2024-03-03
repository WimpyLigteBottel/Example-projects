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
        return FailureAnalysis(description, getAction(), cause)
    }


    private fun getAction(): String {
        return """
                1. Your message should contain 'Hello'
                """.trimIndent()
    }
}