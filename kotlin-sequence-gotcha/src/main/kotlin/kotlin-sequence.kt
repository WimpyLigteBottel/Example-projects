package nel.marco

fun main() {

    firstSequenceExample()
    secondSequenceExample()

}

/**
 * What does sequence do?
 */
fun firstSequenceExample() {
    val result =
        listOf(1, 2, 3)
            .asSequence()
            .take(3)

    println("firstSequenceExample $result")
}

fun secondSequenceExample() {
    val result =
        listOf(1, 2, 3)
            .asSequence()
            .take(3)
            .toList()

    println("firstSequenceExample $result")
}
