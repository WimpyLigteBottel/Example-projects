package nel.marco


fun main() {
    val list = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9")
    associateExample(list)
    chunkExample(list)
    zipExample(list, list)
    partitionExample(list)
}


fun associateExample(list: List<String>) {
    println("associate: " + list.associate { value -> value to value })
    var charL = 'a'.toInt() - 1
    println("associateBy: " + list.associateBy {
        charL++
        charL.toChar()
    })
}


fun chunkExample(list: List<String>) {
    println("Chunked: ${list.chunked(3)}")
}


fun zipExample(list: List<String>, listB: List<String>) {
    println("zip: ${list.zip(listB)}")
}


fun partitionExample(list: List<String>) {
    val listOfSmallList = list.partition { it.toLong() > 5 }
    println("(partition) greater than 5 : ${listOfSmallList.first}")
    println("(partition) smaller than 5 : ${listOfSmallList.second}")
}