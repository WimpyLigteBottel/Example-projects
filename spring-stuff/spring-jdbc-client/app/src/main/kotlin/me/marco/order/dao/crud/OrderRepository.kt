package me.marco.order.dao.crud

import me.marco.order.dao.OrderEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service

@Service
interface OrderRepository : CrudRepository<OrderEntity, Long>