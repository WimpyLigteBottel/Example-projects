package fizzbuzz

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

internal class FizzBuzzTest {

    val fizzBuzz = FizzBuzz()

    @Test
    @DisplayName("expect no exception")
    fun isFizzBuzz() {
        assertDoesNotThrow {
            fizzBuzz.calculate(1);
        }
    }

    @ParameterizedTest(name = "{0} == {1}")
    @CsvSource(
        "1,1",
        "2,2",
        "3,fizz",
        "4,4",
        "5,buzz",
        "6,fizz",
        "7,7",
        "8,8",
        "9,fizz",
        "10,buzz",
        "11,11",
        "12,fizz",
        "13,13",
        "14,14",
        "15,fizzbuzz"
    )
    fun isCorrectTest(number: Int, expected: String) {
        assertEquals(expected, fizzBuzz.calculate(number))
    }
}