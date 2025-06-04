package nel.marco


fun main() {
    val list = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9")
    associateExample(list)
    chunkExample(list)
}


fun associateExample(list: List<String>) {
    val map: Map<String, String> = list.associate { value -> value to value }
    println(map)
}


fun chunkExample(list: List<String>) {
    val listOfSmallList = list.chunked(3)
    println(listOfSmallList)
}