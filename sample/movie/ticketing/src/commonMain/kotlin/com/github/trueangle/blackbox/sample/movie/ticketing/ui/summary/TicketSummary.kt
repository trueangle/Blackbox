package com.github.trueangle.blackbox.sample.movie.ticketing.ui.summary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.trueangle.blackbox.core.IO
import com.github.trueangle.blackbox.multiplatform.ViewModel
import com.github.trueangle.blackbox.multiplatform.rememberViewModel
import com.github.trueangle.blackbox.sample.movie.core.domain.model.User
import com.github.trueangle.blackbox.sample.movie.design.MainButton
import com.github.trueangle.blackbox.sample.movie.ticketing.common.Heading
import com.github.trueangle.blackbox.sample.movie.ticketing.domain.model.Cinema
import com.github.trueangle.blackbox.sample.movie.ticketing.domain.model.Order
import com.github.trueangle.blackbox.sample.movie.ticketing.domain.model.Seat
import com.github.trueangle.blackbox.sample.movie.ticketing.domain.model.ShowTime
import com.github.trueangle.blackbox.sample.movie.ticketing.domain.repository.OrderRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

sealed interface TicketSummaryOutput {
    data object OnRequestUserData : TicketSummaryOutput
    data class OnPurchased(val order: Order) : TicketSummaryOutput
}

sealed interface TicketSummaryInput {
    data class OnUserData(val user: User) : TicketSummaryInput
}

class TicketSummaryIO : IO<TicketSummaryInput, TicketSummaryOutput>()

@Immutable
class TicketSummaryConfig(
    val movieName: String,
    val cinema: Cinema,
    val showTime: ShowTime,
    val seats: ImmutableList<Seat>
)

@Immutable
class TicketSummaryDependencies(
    val orderRepository: OrderRepository
)

@Composable
fun TicketSummary(
    modifier: Modifier,
    dependencies: TicketSummaryDependencies,
    io: TicketSummaryIO,
    config: TicketSummaryConfig
) {

    val viewModel = rememberViewModel {
        TicketSummaryViewModel(io, config, dependencies.orderRepository)
    }

    val state by viewModel.state.collectAsState()

    Scaffold(modifier = modifier, bottomBar = {
        Surface {
            MainButton(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                text = "Pay ${state.totalPriceString}",
                onClick = viewModel::onPayClick,
                enabled = state.userProfile != null,
                progress = state.progress
            )
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(16.dp))

            Heading("Order confirmation")

            Spacer(Modifier.height(32.dp))

            Summary(modifier = Modifier, state)

            Spacer(Modifier.height(12.dp))

            UserProfile(modifier = Modifier, state.userProfile, viewModel::onLoginClick)
        }
    }
}

private class TicketSummaryViewModel(
    private val io: TicketSummaryIO,
    private val config: TicketSummaryConfig,
    private val orderRepository: OrderRepository
) : ViewModel() {

    init {
        coroutineScope.launch {
            io.input.collect {
                when (it) {
                    is TicketSummaryInput.OnUserData -> {
                        state.value = state.value.copy(userProfile = it.user)
                    }
                }
            }
        }
    }

    val state = MutableStateFlow(
        TicketSummaryState(
            cinema = config.cinema,
            showTime = config.showTime,
            seats = config.seats,
            progress = false
        )
    )

    fun onLoginClick() {
        coroutineScope.launch { io.output(TicketSummaryOutput.OnRequestUserData) }
    }

    fun onPayClick() {
        state.value = state.value.copy(progress = true)

        coroutineScope.launch {
            val order = Order(
                movieName = config.movieName,
                cinema = config.cinema,
                seats = config.seats,
                showTime = config.showTime
            )
            orderRepository.createOrder(order)
            io.output(TicketSummaryOutput.OnPurchased(order))
        }
    }
}

private data class TicketSummaryState(
    val cinema: Cinema,
    val showTime: ShowTime,
    val seats: ImmutableList<Seat>,
    val userProfile: User? = null,
    val progress: Boolean
) {
    val totalPriceString = "$" + seats.size * showTime.price
}

@Composable
fun UserProfile(modifier: Modifier, user: User?, onLoginClick: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "User information",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge
            )

            if (user != null) {
                Spacer(Modifier.height(16.dp))

                Row {
                    Text(
                        text = "Name: ",
                        modifier = Modifier.weight(1F),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(Modifier.height(16.dp))

                Row {
                    Text(
                        text = "Email: ",
                        modifier = Modifier.weight(1F),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(Modifier.height(16.dp))

                Row {
                    Text(
                        text = "Phone: ",
                        modifier = Modifier.weight(1F),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = user.phone,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            } else {
                Text(
                    "You need to be authorized in order to pay the tickets",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedButton(
                    onClick = onLoginClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 24.dp),
                ) {
                    Text(text = "Log in", modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}

@Composable
private fun Summary(modifier: Modifier, state: TicketSummaryState) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = state.cinema.name,
                    fontWeight = FontWeight(500),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Text(
                modifier = Modifier,
                text = state.cinema.address,
                style = MaterialTheme.typography.titleSmall,
            )

            Spacer(Modifier.height(16.dp))

            Row {
                Text(
                    text = "Seats: ",
                    modifier = Modifier.weight(1F),
                    style = MaterialTheme.typography.bodyLarge
                )
                Column {
                    state.seats.forEach {
                        Text(
                            text = "Row: ${it.row}, Seat: ${it.seat}",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Row {
                Text(
                    text = "Time: ",
                    modifier = Modifier.weight(1F),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = state.showTime.timeRangeString,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(Modifier.height(12.dp))

            Divider(modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(8.dp))

            Row {
                Text(
                    text = "Price: ",
                    modifier = Modifier.weight(1F),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = state.totalPriceString,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}