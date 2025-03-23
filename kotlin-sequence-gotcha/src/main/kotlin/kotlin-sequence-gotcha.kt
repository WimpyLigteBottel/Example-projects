package nel.marco

fun main() {
    gotcha()
    gotcha2()
    gotcha3()
    gotcha4()
}

/**
 * What will the result be?
 *
 */
fun gotcha() {

    var counter = 0
    listOf(1, 2, 3)
        .asSequence()
        .filter { it != 0 }
        .map { counter++ }

    println("1. gotcha  [counter=$counter]")
}


/**
 * What will the result be?
 */
fun gotcha2() {

    var counter = 0
    listOf(1, 2, 3)
        .asSequence()
        .filter { it != 0 }
        .map { counter++ }
        .toList()

    println("2. gotcha  [counter=$counter]")
}


/**
 * What will the result be?
 */
fun gotcha3() {

    var counter = 0
    listOf(1, 2, 3)
        .asSequence()
        .map { counter++ }
        .take(1) // take added
        .toList()

    println("3. gotcha  [counter=$counter]")
}


/**
 * What will the result be?
 */
fun gotcha4() {

    var counter = 0
    listOf(1, 2, 3)
        //.asSequence()
        .map { counter++ }
        .take(1)
        .toList()

    println("4. gotcha  [counter=$counter]")
}
