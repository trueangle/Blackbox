package com.github.trueangle.blackbox.sample.movie.ticketing.data.repository

import com.github.trueangle.blackbox.sample.movie.ticketing.domain.model.Order
import com.github.trueangle.blackbox.sample.movie.ticketing.domain.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OrderRepositoryImpl: OrderRepository {
    private val ordersCache = MutableStateFlow(emptyList<Order>())

    override fun orders(): StateFlow<List<Order>> = ordersCache

    override suspend fun createOrder(order: Order) {
        ordersCache.emit(ArrayList(ordersCache.value).apply { add(order) })
    }
}