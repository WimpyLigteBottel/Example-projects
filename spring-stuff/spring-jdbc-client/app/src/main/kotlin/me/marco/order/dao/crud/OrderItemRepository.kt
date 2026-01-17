package me.marco.order.dao.crud

import me.marco.order.dao.OrderItemEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service

@Service
interface OrderItemRepository : CrudRepository<OrderItemEntity, Long>