package nel.marco

fun main() {
    gotcha()
    gotcha2()
    gotcha3()
    gotcha4()
    gotcha5()
}

/**
 * What will the result be?
 *
 */
fun gotcha() {

    var counter = 0
    listOf(1, 2, 3)
        .asSequence()
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
        .filter { it != 0 } // filter added
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


/**
 * What will the result be?
 */
fun gotcha5() {

    var counter = 0
    listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        .asSequence()
        .map { counter++ }
        .find { it == 5 }

    println("5. gotcha  [counter=$counter]")
}
