package com.github.trueangle.blackbox.sample.movie.ticketing.ui

import androidx.compose.runtime.Immutable
import com.github.trueangle.blackbox.multiplatform.FlowScope
import com.github.trueangle.blackbox.sample.movie.ticketing.TicketingFlowIO
import com.github.trueangle.blackbox.sample.movie.ticketing.domain.repository.OrderRepository
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.cinema.CinemaListIO
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.seats.SeatsIO
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.summary.TicketSummaryDependencies
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.summary.TicketSummaryIO

@Immutable
class TicketingFlowDependencies(val orderRepository: OrderRepository)

internal class TicketingFlowScope(
    movieName: String,
    dependencies: TicketingFlowDependencies,
    io: TicketingFlowIO
) : FlowScope() {

    val cinemaListIO by lazy { CinemaListIO() }
    val seatsIO by lazy { SeatsIO() }
    val ticketSummaryIO by lazy { TicketSummaryIO() }

    val ticketSummaryDependencies by lazy {
        TicketSummaryDependencies(orderRepository = dependencies.orderRepository)
    }

    init {
        coordinator {
            TicketingFlowCoordinator(
                movieName = movieName,
                flowIO = io,
                cinemaListIO = cinemaListIO,
                seatsIO = seatsIO,
                ticketSummaryIO = ticketSummaryIO
            )
        }
    }
}
