package me.marco.order.dao

import me.marco.order.dao.crud.OrderRepository
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
open class OrderDao(
    private val jdbc: JdbcClient,
    private val jdbcNamed: NamedParameterJdbcTemplate,
    private val repository: OrderRepository
) {

    /* -------------------- V1: JdbcClient -------------------- */

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

    fun getOrder(orderId: Long): Optional<OrderEntity> {
        val order = jdbc.sql(
            """
            SELECT *
            FROM orders
            WHERE order_id = :orderId
            """
        )
            .param("orderId", orderId)
            .query(::mapOrder)
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
                if (!rs.wasNull()) {
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

        if (orders.isEmpty()) return Optional.empty()
        return Optional.ofNullable(orders.getValue(orderId).copy(items = items.values.toList()))
    }

    fun getAllOrders(orderIds: List<Long>): List<OrderEntity> {
        return jdbc.sql(
            """
            SELECT *
            FROM orders
            WHERE order_id in (:orderIds)
            """
        )
            .param("orderIds", orderIds)
            .query(::mapOrder)
            .list()
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

    /* -------------------- V2: NamedParameterJdbcTemplate -------------------- */

    fun createOrderV2(): Long {
        val sql = """
            INSERT INTO orders (total_amount, is_paid, version)
            VALUES (:totalAmount, :isPaid, :version)
            RETURNING order_id
        """.trimIndent()

        val params = MapSqlParameterSource()
            .addValue("totalAmount", 0)
            .addValue("isPaid", false)
            .addValue("version", 0)

        return jdbcNamed.queryForObject(sql, params, Long::class.java)!!
    }

    fun getOrderV2(orderId: Long): Optional<OrderEntity> {
        val sql = """
            SELECT *
            FROM orders
            WHERE order_id = :orderId
        """.trimIndent()

        val params = MapSqlParameterSource().addValue("orderId", orderId)

        val order = jdbcNamed.query(sql, params, mapOrderRowMapper)
        return order.firstOrNull()?.let { Optional.of(it) } ?: Optional.empty()
    }

    fun getOrderWithItemsV2(orderId: Long): Optional<OrderEntity> {
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

        val params = MapSqlParameterSource().addValue("orderId", orderId)

        val orders = linkedMapOf<Long, OrderEntity>()
        jdbcNamed.query(sql, params) { rs ->
            val oid = rs.getLong("order_id")
            val order = orders.getOrPut(oid) {
                OrderEntity(
                    orderId = oid,
                    totalAmount = rs.getDouble("total_amount"),
                    isPaid = rs.getBoolean("is_paid"),
                    version = rs.getLong("version"),
                    items = mutableListOf()
                )
            }

            val itemId = rs.getLong("item_id")
            if (!rs.wasNull()) {
                order.items.add(
                    OrderItemEntity(
                        id = itemId,
                        orderId = oid,
                        item = rs.getString("item")
                    )
                )
            }
        }

        return orders[orderId]?.let { Optional.of(it) } ?: Optional.empty()
    }

    fun getAllOrdersV2(orderIds: List<Long>): List<OrderEntity> {
        val sql = """
            SELECT *
            FROM orders
            WHERE order_id IN (:orderIds)
        """.trimIndent()

        val params = MapSqlParameterSource().addValue("orderIds", orderIds)

        return jdbcNamed.query(sql, params, mapOrderRowMapper)
    }

    @Transactional
    fun deleteOrderV2(orderId: String) {
        val sql = "DELETE FROM orders WHERE order_id = :orderId"
        val params = MapSqlParameterSource().addValue("orderId", orderId.toLong())
        jdbcNamed.update(sql, params)
    }

    fun deleteOrdersV2() {
        val sql = "DELETE FROM orders"
        jdbcNamed.update(sql, MapSqlParameterSource())
    }

    /* -------------------- MAPPERS -------------------- */

    private val mapOrderRowMapper = RowMapper { rs, _ ->
        OrderEntity(
            orderId = rs.getLong("order_id"),
            totalAmount = rs.getDouble("total_amount"),
            isPaid = rs.getBoolean("is_paid"),
            version = rs.getLong("version"),
            items = mutableListOf()
        )
    }

    private fun mapOrder(rs: java.sql.ResultSet, _result: Int) = OrderEntity(
        orderId = rs.getLong("order_id"),
        totalAmount = rs.getDouble("total_amount"),
        isPaid = rs.getBoolean("is_paid"),
        version = rs.getLong("version"),
        items = mutableListOf()
    )
}
