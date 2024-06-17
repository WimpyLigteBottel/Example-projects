import nel.marco.DataClassExample
import nel.marco.ParameterHelperDataHolder
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class DataClassExampleTest {


    var example = DataClassExample()

    @Test
    fun `test example method`() {

        val actual = example.generateExample(emptyList()).toString()

        assertThat(actual)
            .isEqualToIgnoringWhitespace("public data class Person()")
    }

    @Test
    fun `test example method with 1 parameter string added`() {


        val parameter = listOf(
            ParameterHelperDataHolder(
                name = "name",
                type = String::class,
                isMutable = false,
                defaultValue = ""
            )
        )

        val actual = example.generateExample(parameter).toString()

        val expected = """
            public data class Person(
                public val name : kotlin.String,
            )
        """.trimIndent()

        assertThat(actual)
            .isEqualToIgnoringWhitespace(expected)
    }

    @Test
    fun `test example method with multiple parameters string added`() {

        val parameter = listOf(
            ParameterHelperDataHolder(
                name = "name",
                type = String::class,
                isMutable = false,
                defaultValue = ""
            ),
            ParameterHelperDataHolder(
                name = "age",
                type = Int::class,
                isMutable = false,
                defaultValue = ""
            )
        )


        val actual = example.generateExample(parameter).toString()

        val expected = """
            public data class Person(
                public val name : kotlin.String,
                public val age : kotlin.Int,
            )
        """.trimIndent()

        assertThat(actual)
            .isEqualToIgnoringWhitespace(expected)
    }
    @Test
    fun `test example method to see if isMutable works`() {

        val parameter = listOf(
            ParameterHelperDataHolder(
                name = "name",
                type = String::class,
                isMutable = true,
                defaultValue = ""
            )
        )


        val actual = example.generateExample(parameter).toString()

        val expected = """
            public data class Person(
                public var name : kotlin.String,
            )
        """.trimIndent()

        assertThat(actual)
            .isEqualToIgnoringWhitespace(expected)
    }
}