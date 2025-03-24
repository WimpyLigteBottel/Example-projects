package nel.marco


fun main() {
    collectionExample()
    sequenceExample()
}


fun sequenceExample() {
    var counter = 0
    listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        .asSequence()
        .map {
            counter++
            it
        }
        .take(3)
        .toList()


    println("Sequence counter: $counter")
}


fun collectionExample() {
    var counter = 0
    listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        .filter { it > 3 }
        .map {
            counter++
            it
        }
        .take(3)


    println("collection counter: $counter")
}
