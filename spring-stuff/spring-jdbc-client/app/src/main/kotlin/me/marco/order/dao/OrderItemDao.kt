package me.marco.order.dao

import me.marco.order.dao.crud.OrderItemRepository
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Service

@Service
open class OrderItemDao(
    private val jdbc: JdbcClient,
    private val repository: OrderItemRepository
) {


    fun addItem(orderId: Long, item: String): Long {
        return jdbc.sql(
            """
            INSERT INTO order_items (order_id, item)
            VALUES (:orderId, :item)
            RETURNING id
            """
        )
            .param("orderId", orderId)
            .param("item", item)
            .query(Long::class.java)
            .single()
    }

    fun removeItem(itemId: Long) {
        jdbc.sql(
            "DELETE FROM order_items WHERE id = :id"
        )
            .param("id", itemId)
            .update()
    }

    fun deleteItems() {
        jdbc.sql(
            "DELETE FROM order_items"
        )
            .update()
    }

}