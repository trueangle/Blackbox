package io.github.trueangle.blackbox.sample.movie.ticketing.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import io.github.trueangle.blackbox.sample.movie.ticketing.TicketingFlow
import io.github.trueangle.blackbox.sample.movie.ticketing.TicketingFlowIO
import io.github.trueangle.blackbox.sample.movie.ticketing.data.repository.OrderRepositoryImpl
import io.github.trueangle.blackbox.sample.movie.ticketing.ui.orders.Orders
import io.github.trueangle.blackbox.sample.movie.ticketing.ui.orders.OrdersDependencies
import io.ktor.client.HttpClient

@Immutable
class TicketingConfig(val movieName: String)

@Immutable
class TicketingDependencies(httpClient: HttpClient)

@Immutable
class TicketingFactory(private val dependencies: TicketingDependencies) {
    private val orderRepository by lazy { OrderRepositoryImpl() }

    @Composable
    fun CreateTicketingFlow(
        modifier: Modifier,
        io: TicketingFlowIO,
        config: TicketingConfig
    ) = TicketingFlow(modifier, TicketingFlowDependencies(orderRepository), io, config)

    @Composable
    fun Orders(modifier: Modifier) = Orders(
        modifier = modifier,
        OrdersDependencies(orderRepository)
    )
}
