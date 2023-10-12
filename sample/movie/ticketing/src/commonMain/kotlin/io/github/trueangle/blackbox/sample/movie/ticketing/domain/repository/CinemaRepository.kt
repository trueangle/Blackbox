package io.github.trueangle.blackbox.sample.movie.ticketing.domain.repository

import androidx.compose.runtime.Immutable
import io.github.trueangle.blackbox.sample.movie.ticketing.domain.model.Cinema
import io.github.trueangle.blackbox.sample.movie.ticketing.domain.model.Order
import kotlinx.coroutines.flow.StateFlow

@Immutable
interface CinemaRepository {
    fun getAll(): List<Cinema>
}

@Immutable
interface OrderRepository {
    fun orders(): StateFlow<List<Order>>
    suspend fun createOrder(order: Order)
}