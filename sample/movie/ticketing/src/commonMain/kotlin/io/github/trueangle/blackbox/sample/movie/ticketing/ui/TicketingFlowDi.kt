package io.github.trueangle.blackbox.sample.movie.ticketing.ui

import androidx.compose.runtime.Immutable
import io.github.trueangle.blackbox.multiplatform.Coordinator
import io.github.trueangle.blackbox.multiplatform.FlowScope
import io.github.trueangle.blackbox.sample.movie.ticketing.TicketingFlowIO
import io.github.trueangle.blackbox.sample.movie.ticketing.domain.repository.OrderRepository
import io.github.trueangle.blackbox.sample.movie.ticketing.ui.cinema.CinemaListIO
import io.github.trueangle.blackbox.sample.movie.ticketing.ui.seats.SeatsIO
import io.github.trueangle.blackbox.sample.movie.ticketing.ui.summary.TicketSummaryDependencies
import io.github.trueangle.blackbox.sample.movie.ticketing.ui.summary.TicketSummaryIO

@Immutable
class TicketingFlowDependencies(val orderRepository: OrderRepository)

internal class TicketingFlowScope(
    config: TicketingConfig,
    dependencies: TicketingFlowDependencies,
    io: TicketingFlowIO
) : FlowScope() {

    val cinemaListIO by lazy { CinemaListIO() }
    val seatsIO by lazy { SeatsIO() }
    val ticketSummaryIO by lazy { TicketSummaryIO() }
    val ticketSummaryDependencies by lazy {
        TicketSummaryDependencies(orderRepository = dependencies.orderRepository)
    }

    override val coordinator by lazy {
        TicketingFlowCoordinator(
            movieName = config.movieName,
            flowIO = io,
            cinemaListIO = cinemaListIO,
            seatsIO = seatsIO,
            ticketSummaryIO = ticketSummaryIO
        )
    }
}
