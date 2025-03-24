package fizzbuzz

class FizzBuzz {

    fun calculate(input: Int): String {
        if (isFizzBuzz(input))
            return "fizzbuzz"
        if (isFizz(input))
            return "fizz"
        if (isBuzz(input))
            return "buzz"

        return input.toString()
    }

    private fun isFizzBuzz(input: Int) = isFizz(input) && isBuzz(input)

    private fun isFizz(input: Int) = input % 3 == 0

    private fun isBuzz(input: Int) = input % 5 == 0
}