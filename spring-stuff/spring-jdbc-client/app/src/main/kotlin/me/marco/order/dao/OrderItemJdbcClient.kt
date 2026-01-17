package me.marco.order.dao

import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Service
import java.sql.ResultSet

@Service
open class OrderItemJdbcClient(
    private val jdbc: JdbcClient
) {

    private fun getItems(orderId: String): List<OrderItemEntity> =
        jdbc.sql(
            """
            SELECT id, order_id, item
            FROM order_items
            WHERE order_id = :orderId
            """
        )
            .param("orderId", orderId)
            .query(::mapItem)
            .list()

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

    private fun getAllItems(): List<OrderItemEntity> =
        jdbc.sql(
            """
            SELECT id, order_id, item
            FROM order_items
            """
        )
            .query(::mapItem)
            .list()

    fun updateItem(item: OrderItemEntity) {
        jdbc.sql(
            """
            UPDATE order_items
            SET item = :item
            WHERE id = :id
            """
        )
            .paramSource(item)
            .update()
    }

    /* -------------------- DELETE -------------------- */

    fun removeItem(itemId: Long) {
        jdbc.sql(
            "DELETE FROM order_items WHERE id = :id"
        )
            .param("id", itemId)
            .update()
    }

    fun removeItems(orderId: String) {
        jdbc.sql(
            "DELETE FROM order_items WHERE order_id = :orderId"
        )
            .param("orderId", orderId)
            .update()
    }

    fun deleteItems() {
        jdbc.sql(
            "DELETE FROM order_items"
        )
            .update()
    }

    private fun mapItem(rs: ResultSet, _result: Int) = OrderItemEntity(
        id = rs.getLong("id"),
        orderId = rs.getLong("order_id"),
        item = rs.getString("item")
    )
}