package me.marco.order.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service

@Service
interface OrderItemRepository : CrudRepository<OrderItemEntity, Long>