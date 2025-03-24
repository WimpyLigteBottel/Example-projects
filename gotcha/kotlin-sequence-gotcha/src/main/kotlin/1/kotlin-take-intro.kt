package nel.marco

fun main() {

    firstExample()
    secondExample()
    thirdExample()
    fourthExample()

}


/**
 * What do you think the take operation does?
 *
 * Question: What do you think the result will be?
 *
 *
 * ```
 *  - A -> 1,2,3
 *  - B -> 3
 *  - C -> exception?
 * ```
 */
fun firstExample() {
    val result =
        listOf(1, 2, 3)
            .take(3)

    println("firstExample $result")
}


/**
 * Question: What do you think the result will be?
 *
 * ```
 * - A -> 1,2,3
 * - B -> 3
 * - C -> exception?
 * ```
 */
fun secondExample() {
    val result =
        listOf(1, 2, 3)
            .filter { it > 2 } // filter has been added
            .take(3)

    println("secondExample $result")
}


/**
 * Question: What do you think the result will be?
 *
 * ```
 * - A -> 2
 * - B -> 3
 * - C -> exception?
 * ```
 */
fun thirdExample() {
    val result =
        listOf(1, 2, 3)
            .filter { it > 1 } // filter was 2
            .take(1)  // take was 3

    println("thirdExample $result")
}



/**
 * Question: What do you think the result will be?
 *
 *  ```
 *  - A -> 2
 *  - B -> 3
 *  - C -> exception?
 *  ```
 */
fun fourthExample() {
    val result =
        listOf(1, 2, 3)
            .filter { it > 1 }
            .takeLast(1) // was take

    println("fourthExample $result")
}