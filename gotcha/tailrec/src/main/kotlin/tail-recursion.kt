package nel.marco.recursion


fun main() {
    runCatching {
        fibonacci(1000000, 0, 1)
    }
        .onFailure {
            println("FAIL")
        }
        .onSuccess {
            println("SUCCESS")
        }
}


fun fibonacci(remaining: Int, a: Long = 0, b: Long = 1): Long {
    return if (remaining == 0)
        a
    else {
        fibonacci(remaining - 1, b, a + b)
    }
}