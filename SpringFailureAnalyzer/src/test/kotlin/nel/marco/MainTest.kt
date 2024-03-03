package nel.marco

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertTrue


@SpringBootTest
class MainTest {

    @Autowired
    private lateinit var customFailureAnalyzer: FailureAnalyzer


    @Test
    fun testCustomFailureAnalyzer() {
        try {
            throw InvalidHelloMessageException("I SHOULD FAIL")
        } catch (e: InvalidHelloMessageException) {
            val failureAnalysis = customFailureAnalyzer.analyze(e)
            // You can assert on the failure analysis or log it
            assertTrue { failureAnalysis.description.contains("You have received weird exception with message 'I SHOULD FAIL'") }

            assertThat(failureAnalysis.action)
                .contains("Remove the exception that is throwing this")
                .contains("Surround your code with tryCatch")
                .contains("Hope for the best ;)")
        }
    }

}