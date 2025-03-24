import nel.marco.FunctionAnnotation
import nel.marco.FunctionDataHolder
import nel.marco.FunctionParameter
import nel.marco.FunctionExample
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class FunctionExampleTest {

    var example = FunctionExample()

    @Test
    fun `test example method`() {

        val holder = FunctionDataHolder(
            name = "functionName",
            returnType = Unit::class,
            parameters = emptyList()
        )

        val actual = example.generateExample(holder).toString()

        assertThat(actual)
            .isEqualToIgnoringWhitespace("public fun functionName() {}")
    }

    @Test
    fun `test example function with annotation`() {

        val holder = FunctionDataHolder(
            name = "functionName",
            returnType = Unit::class,
            parameters = emptyList(),
            annotations = listOf(
                FunctionAnnotation(
                    "depricated",
                    Deprecated::class,
                    "message"
                )
            )
        )

        val actual = example.generateExample(holder).toString()

        assertThat(actual)
            .isEqualToIgnoringWhitespace(
                """
                @kotlin.Deprecated("message")
                public fun functionName() {
                }
            """.trimIndent()
            )
    }


    @Test
    fun `test example method with return type`() {

        val holder = FunctionDataHolder(
            name = "functionName",
            returnType = String::class,
            parameters = emptyList()
        )

        val actual = example.generateExample(holder).toString()

        assertThat(actual)
            .isEqualToIgnoringWhitespace("public fun functionName(): kotlin.String {}")
    }

    @Test
    fun `test example method with 1 parameter string added`() {


        val parameters = listOf(
            FunctionParameter(
                name = "name",
                type = String::class,
                defaultValue = ""
            )
        )

        val holder = FunctionDataHolder(
            name = "functionName",
            returnType = Unit::class,
            parameters = parameters
        )

        val actual = example.generateExample(holder).toString()

        val expected = """
                public fun functionName(name: kotlin.String) {
                    
                }
        """.trimIndent()

        assertThat(actual)
            .isEqualToIgnoringWhitespace(expected)
    }

    @Test
    fun `test example method with multiple parameters string added`() {

        val parameter = listOf(
            FunctionParameter(
                name = "name",
                type = String::class,
                defaultValue = ""
            ),
            FunctionParameter(
                name = "age",
                type = Int::class,
                defaultValue = ""
            )
        )

        val holder = FunctionDataHolder(
            name = "functionName",
            returnType = Unit::class,
            parameters = parameter
        )

        val actual = example.generateExample(holder).toString()

        val expected = """
            public fun functionName(
                name: kotlin.String,
                age: kotlin.Int
            ) {
                
            }
        """.trimIndent()

        assertThat(actual)
            .isEqualToIgnoringWhitespace(expected)
    }

}