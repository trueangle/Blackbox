package com.github.trueangle.blackbox.sample.movie.ticketing

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.trueangle.blackbox.sample.movie.ticketing.common.FlowTopAppBar
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.TicketingFlowCoordinator
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.TicketingFlowDependencies
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.TicketingFlowScope
import com.github.trueangle.blackbox.core.IO
import com.github.trueangle.blackbox.multiplatform.NavigationFlow
import com.github.trueangle.blackbox.multiplatform.rememberScope
import com.github.trueangle.blackbox.sample.movie.core.domain.model.User
import com.github.trueangle.blackbox.sample.movie.ticketing.domain.model.Order
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.cinema.CinemaList
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.seats.Seats
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.seats.SeatsConfig
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.summary.TicketSummary
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.summary.TicketSummaryConfig

sealed interface TicketingFlowOutput {
    data object OnClose : TicketingFlowOutput
    data class OnPurchased(val order: Order) : TicketingFlowOutput
    data object OnRequestAuth : TicketingFlowOutput
}

sealed interface TicketingFlowInput {
    data class OnUserAuthorized(val user: User) : TicketingFlowInput
}

class TicketingFlowIO : IO<TicketingFlowInput, TicketingFlowOutput>()

internal sealed interface FlowRoutes {
    data object CinemaList : FlowRoutes {
        const val RoutePattern = "cinemas"
    }

    data object Seats : FlowRoutes {
        const val RoutePattern = "seats"
    }

    data object Summary : FlowRoutes {
        const val RoutePattern = "summary"
    }
}

@Composable
internal fun TicketingFlow(
    dependencies: TicketingFlowDependencies,
    io: TicketingFlowIO,
    movieName: String
) {
    val scope =
        rememberScope(TicketingFlowScope::class) { TicketingFlowScope(movieName, dependencies, io) }

    val coordinator = scope.coordinator as TicketingFlowCoordinator
    val currentRoute by scope.coordinator.currentRoute.collectAsState(FLOW_STEPS.first())

    val stepIndex by remember {
        derivedStateOf {
            FLOW_STEPS.indexOf(currentRoute)
        }
    }

    Surface(modifier = Modifier.supportWideScreen()) {
        Scaffold(
            topBar = {
                FlowTopAppBar(
                    stepIndex = stepIndex,
                    totalStepsCount = FLOW_STEPS.size,
                    onClosePressed = coordinator::onToolbarCloseClick,
                )
            },
            content = {
                NavigationContent(
                    modifier = Modifier.padding(it).fillMaxSize(),
                    scope,
                    movieName
                )
            }
        )
    }
}

private val FLOW_STEPS = listOf(
    FlowRoutes.CinemaList.RoutePattern,
    FlowRoutes.Seats.RoutePattern,
    FlowRoutes.Summary.RoutePattern
)

@Composable
private fun NavigationContent(modifier: Modifier, scope: TicketingFlowScope, movieName: String) {
    val coordinator = scope.coordinator as TicketingFlowCoordinator

    NavigationFlow(
        modifier = modifier,
        startDestination = FlowRoutes.CinemaList.RoutePattern,
        coordinator = scope.coordinator,
        persistNavState = false
    ) {

        scene(route = FlowRoutes.CinemaList.RoutePattern) {
            CinemaList(modifier = Modifier, movieName = movieName, io = scope.cinemaListIO)
        }

        scene(route = FlowRoutes.Seats.RoutePattern) {
            Seats(
                modifier = Modifier,
                io = scope.seatsIO,
                config = SeatsConfig(
                    cinema = requireNotNull(coordinator.flowState.selectedCinema),
                    showTime = requireNotNull(coordinator.flowState.selectedShowTime)
                )
            )
        }

        scene(route = FlowRoutes.Summary.RoutePattern) {
            TicketSummary(
                modifier = Modifier,
                io = scope.ticketSummaryIO,
                config = TicketSummaryConfig(
                    movieName = movieName,
                    cinema = requireNotNull(coordinator.flowState.selectedCinema),
                    showTime = requireNotNull(coordinator.flowState.selectedShowTime),
                    seats = requireNotNull(coordinator.flowState.selectedSeats)
                ),
                dependencies = scope.ticketSummaryDependencies
            )
        }
    }
}

internal fun Modifier.supportWideScreen() = this
    .fillMaxWidth()
    .wrapContentWidth(align = Alignment.CenterHorizontally)
    .widthIn(max = 840.dp)
