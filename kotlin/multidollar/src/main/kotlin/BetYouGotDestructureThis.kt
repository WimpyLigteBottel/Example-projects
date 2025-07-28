data class Person(
    val name: String,
    val age: Int
)


fun main() {
    val (name, age) = Person("Marco", 1)

    println("$name $age")
}
