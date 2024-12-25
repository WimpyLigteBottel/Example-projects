import nel.marco.InterfaceGeneratorExample
import nel.marco.ParameterHelperDataHolder
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class InterfaceGeneratorExampleTest {
    var example = InterfaceGeneratorExample()

    val parent =
        example.generateExample(
            interfaceName = "ParentI",
            parameter =
                listOf(
                    ParameterHelperDataHolder(
                        name = "name",
                        type = String::class,
                        isMutable = false,
                        defaultValue = "",
                    ),
                ),
        )
    val human =
        example.generateExample(
            interfaceName = "HumanI",
            parameter =
                listOf(
                    ParameterHelperDataHolder(
                        name = "age",
                        type = Int::class,
                        isMutable = false,
                        defaultValue = "",
                    ),
                ),
        )

    val blank = example.generateExample(interfaceName = "HumanParent", parameter = listOf())

    @Test
    fun generateExample() {
        assertThat(parent.toString()).isEqualToIgnoringWhitespace(
            """
        public interface Parent {
          public val name: kotlin.String
        }
        """,
        )
        assertThat(human.toString()).isEqualToIgnoringWhitespace(
            """
        public interface Human {
          public val age: kotlin.Int
        }
        """,
        )
    }

    @Test
    fun joinInterfaces() {
        val combine = example.combine(blank, listOf(human, parent))

        println(human.toString())
        println(parent.toString())
        println(combine.toString())
    }
}
