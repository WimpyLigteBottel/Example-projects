package nel.marco

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals
import kotlin.test.assertTrue


internal class SolverTest {

    val solver: Solver = Solver()

    @Test
    fun solve() {

        val input = """
            ?,8,6,7,1,2,5,9,?
            9,1,5,3,6,4,7,2,8
            2,7,4,8,5,9,3,1,6
            1,5,8,9,3,7,6,4,2
            4,2,3,6,8,5,9,7,1
            7,6,9,2,4,1,8,5,3
            6,4,1,5,9,8,2,3,7
            8,9,2,4,7,3,1,6,5
            5,3,7,1,2,6,4,8,?
        """.trimIndent()


        assertTrue { solver.solve(solver.convert(input)) }

    }

    @Test
    fun convertSimple() {
        val actual = solver.convert(
            """
            1,2,3,4
            """.trimIndent()
        )

        assertEquals(listOf(listOf(1, 2, 3, 4)).toString(), actual.toString())
    }

    @Test
    fun convertComplex() {
        var text = """
           1,2,3,4
           4,2,3,4
        """.trimIndent();

        val actual = solver.convert(text)

        assertEquals(listOf(listOf(1, 2, 3, 4), listOf(4, 2, 3, 4)).toString(), actual.toString())
    }

    @Test
    fun convertComplex2() {
        var text = """
           1,2,3,4
           4,2,3,4
        """.trimIndent();

        val actual = solver.convert(text)

        assertEquals("1", actual[0][0])
        assertEquals("4", actual[1][0])
        assertEquals("2", actual[0][1])
    }

    @ParameterizedTest
    @CsvSource(
        value = [
//            "1,2,3,4,5,6,7,8,9#true",
            "1,2,3,4,5,6,7,9,9#false",
            "1,a,3,4,5,6,7,8,9#false",
            "1,b,3,4,5,6,7,8,9#false",
            "1,?,3,4,5,6,7,8,9#false"
        ], delimiterString = "#"
    )
    fun isValid_nonUniqueCharacters(input: String, expected: Boolean) {


        var actual = solver.isValid(solver.convert(input))


        assertEquals(expected, actual)
    }

    @Test
    fun isValid_topTooBottomIsValid() {

        val input = """
            3,8,6,7,1,2,5,9,4
            9,1,5,3,6,4,7,2,8
            2,7,4,8,5,9,3,1,6
            1,5,8,9,3,7,6,4,2
            4,2,3,6,8,5,9,7,1
            7,6,9,2,4,1,8,5,3
            6,4,1,5,9,8,2,3,7
            8,9,2,4,7,3,1,6,5
            5,3,7,1,2,6,4,8,9
        """.trimIndent()

        var actual = solver.isValid(solver.convert(input))


        assertEquals(true, actual)
    }

    @Test
    fun isValid_topTooBottomIsInValid() {

        val input = """
            3,8,6,7,1,2,5,9,4
            3,1,5,3,6,4,7,2,8
            2,7,4,8,5,9,3,1,6
            1,5,8,9,3,7,6,4,2
            4,2,3,6,8,5,9,7,1
            7,6,9,2,4,1,8,5,3
            6,4,1,5,9,8,2,3,7
            8,9,2,4,7,3,1,6,5
            5,3,7,1,2,6,4,8,9
        """.trimIndent()

        var actual = solver.isValid(solver.convert(input))


        assertEquals(false, actual)
    }

    @Test
    fun isValid_topTooBottomIsInValid2() {

        val input = """
            3,8,6,7,1,2,5,9,4
            9,1,5,3,6,4,7,2,8
            2,7,4,8,5,9,3,1,6
            1,5,8,9,3,7,6,4,2
            4,2,3,6,8,5,9,7,1
            7,6,9,2,4,1,8,5,3
            6,4,1,5,9,8,2,3,7
            8,9,2,4,7,3,1,6,5
            5,10,7,1,2,6,4,8,9
        """.trimIndent()

        var actual = solver.isValid(solver.convert(input))


        assertEquals(false, actual)
    }
}