package me.marco.order.dao

import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
open class OrderJdbcClient(
    private val jdbc: JdbcClient
) {

    fun createOrder(): Long {
        return jdbc.sql(
            """
        INSERT INTO orders (total_amount, is_paid, version)
        VALUES (:totalAmount, :isPaid, :version)
        RETURNING order_id
        """
        )
            .param("totalAmount", 0)
            .param("isPaid", false)
            .param("version", 0)
            .query(Long::class.java)
            .single()
    }

    /* -------------------- READ -------------------- */

    fun getOrder(orderId: Long): Optional<OrderEntity> {
        val order = jdbc.sql(
            """
            SELECT *
            FROM orders
            WHERE order_id = :orderId
            """
        )
            .param("orderId", orderId)
            .query { rs, rowNum ->
                mapOrder(rs, rowNum)
            }
            .optional()

        return order
    }


    fun getOrderWithItems(orderId: Long): Optional<OrderEntity> {
        val sql = """
            SELECT
                o.order_id,
                o.total_amount,
                o.is_paid,
                o.version,
                i.id AS item_id,
                i.item AS item
            FROM orders o
            LEFT JOIN order_items i ON i.order_id = o.order_id
            WHERE o.order_id = :orderId
            ORDER BY o.order_id
        """.trimIndent()

        val orders = linkedMapOf<Long, OrderEntity>()
        val items = linkedMapOf<Long, OrderItemEntity>()

        jdbc.sql(sql)
            .param("orderId", orderId)
            .query { rs ->
                val orderId = rs.getLong("order_id")

                orders.getOrPut(orderId) {
                    OrderEntity(
                        orderId = orderId,
                        totalAmount = rs.getDouble("total_amount"),
                        isPaid = rs.getBoolean("is_paid"),
                        version = rs.getLong("version"),
                        items = emptyList()
                    )
                }

                val itemId = rs.getLong("item_id")

                if(!rs.wasNull()){
                    val item = rs.getString("item")
                    items.getOrPut(itemId) {
                        OrderItemEntity(
                            id = itemId,
                            orderId = orderId,
                            item = item
                        )
                    }
                }
            }

        if (orders.isEmpty()) {
            return Optional.empty()
        }

        return Optional.ofNullable(orders.getValue(orderId).copy(items = items.values.toList()))
    }

    fun getAllOrders(orderIds: List<Long>): List<OrderEntity> {
        val orders = jdbc.sql(
            """
            SELECT *
            FROM orders
            WHERE order_id in (:orderIds)
            """
        )
            .param("orderIds", orderIds)
            .query(::mapOrder)
            .list()


        return orders
    }

    /* -------------------- UPDATE -------------------- */

    @Transactional
    fun updateOrder(order: OrderEntity) {
        val updated = jdbc.sql(
            """
            UPDATE orders
            SET total_amount = :totalAmount,
                is_paid = :isPaid,
                version = version + 1
            WHERE order_id = :orderId
              AND version = :version
            """
        )
            .paramSource(order)
            .update()

        if (updated == 0) {
            throw IllegalStateException("Order version conflict")
        }

    }


    @Transactional
    fun deleteOrder(orderId: String) {
        jdbc.sql(
            "DELETE FROM orders WHERE order_id = :orderId"
        )
            .param("orderId", orderId.toLong())
            .update()
    }

    fun deleteOrders() {
        jdbc.sql(
            "DELETE FROM orders"
        )
            .update()
    }

    /* -------------------- MAPPERS -------------------- */

    private fun mapOrder(rs: java.sql.ResultSet, _result: Int) = OrderEntity(
        orderId = rs.getLong("order_id"),
        totalAmount = rs.getDouble("total_amount"),
        isPaid = rs.getBoolean("is_paid"),
        version = rs.getLong("version"),
        items = emptyList()
    )

}