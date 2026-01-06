package me.marco.order

data class OrderItem(
    val itemId: String,
    val name: String,
    val price: Double,
    val quantity: Int
)

data class Order(
    val id: String,
    val items: List<OrderItem> = emptyList(),
    val isPaid: Boolean = false,
    val totalAmount: Double = items.sumOf { it.price * it.quantity },
    val version: Long = 0
) {

    fun incrementVersion(): Order {
        return copy(version = version + 1)
    }
}