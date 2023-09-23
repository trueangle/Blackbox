package com.github.trueangle.blackbox.sample.movie.ticketing.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.github.trueangle.blackbox.sample.movie.ticketing.TicketingFlow
import com.github.trueangle.blackbox.sample.movie.ticketing.TicketingFlowIO
import com.github.trueangle.blackbox.sample.movie.ticketing.data.repository.OrderRepositoryImpl
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.orders.Orders
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.orders.OrdersDependencies
import io.ktor.client.HttpClient

@Immutable
class TicketingDependencies(httpClient: HttpClient)

@Immutable
class TicketingFactory(private val dependencies: TicketingDependencies) {
    private val orderRepository by lazy { OrderRepositoryImpl() }

    @Composable
    fun TicketingFlow(
        io: TicketingFlowIO,
        movieName: String
    ) = TicketingFlow(TicketingFlowDependencies(orderRepository), io, movieName)

    @Composable
    fun Orders(modifier: Modifier) = Orders(
        modifier = modifier,
        OrdersDependencies(orderRepository)
    )
}
