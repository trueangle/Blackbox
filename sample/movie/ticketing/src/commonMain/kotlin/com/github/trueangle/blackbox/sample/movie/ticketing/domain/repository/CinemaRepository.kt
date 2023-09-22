package com.github.trueangle.blackbox.sample.movie.ticketing.domain.repository

import com.github.trueangle.blackbox.sample.movie.ticketing.domain.model.Cinema
import com.github.trueangle.blackbox.sample.movie.ticketing.domain.model.Order
import kotlinx.coroutines.flow.StateFlow

interface CinemaRepository {
    fun getAll(): List<Cinema>
}

interface OrderRepository {
    fun orders(): StateFlow<List<Order>>
    suspend fun createOrder(order: Order)
}