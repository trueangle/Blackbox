package com.github.trueangle.blackbox.sample.movie.ticketing.ui.seats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.trueangle.blackbox.core.IO
import com.github.trueangle.blackbox.multiplatform.ViewModel
import com.github.trueangle.blackbox.multiplatform.ViewModelScope
import com.github.trueangle.blackbox.multiplatform.rememberScope
import com.github.trueangle.blackbox.multiplatform.rememberViewModel
import com.github.trueangle.blackbox.sample.movie.ticketing.domain.model.Cinema
import com.github.trueangle.blackbox.sample.movie.ticketing.domain.model.Seat
import com.github.trueangle.blackbox.sample.movie.ticketing.domain.model.ShowTime
import com.github.trueangle.blackbox.sample.movie.ticketing.ui.cinema.CinemaRow
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

sealed interface SeatsOutput {
    data class OnFinish(val seats: ImmutableList<Seat>) : SeatsOutput
}

class SeatsIO : IO<Nothing, SeatsOutput>()
class SeatsConfig(
    val cinema: Cinema,
    val showTime: ShowTime
)

private const val SEAT_ROWS = 8
private const val SEATS_IN_A_ROW = 6

private class SeatsViewModel(private val io: SeatsIO, config: SeatsConfig) : ViewModel() {
    val seats = MutableStateFlow<ImmutableList<Seat>>(makeSeats())
    val cinema = config.toCinemaState()
    val buttonEnabled = seats.map { it.count { item -> item.selected } > 0 }

    fun makeSeats(): PersistentList<Seat> {
        val seats = mutableListOf<Seat>()

        repeat(SEAT_ROWS) { row ->
            repeat(SEATS_IN_A_ROW) { place ->
                seats.add(
                    Seat(
                        row = row + 1,
                        seat = place + 1,
                        selected = false
                    )
                )
            }
        }

        return seats.toPersistentList()
    }

    fun onItemClick(selectedItem: Seat) {
        val oldState = seats.value
        seats.value = oldState.map {
            if (it == selectedItem) {
                it.copy(selected = !it.selected)
            } else {
                it
            }
        }.toPersistentList()
    }

    fun onNextClick() {
        coroutineScope.launch {
            io.output(SeatsOutput.OnFinish(seats.value.filter { it.selected }.toImmutableList()))
        }
    }

    private fun SeatsConfig.toCinemaState() = cinema.copy(
        showTimes = cinema.showTimes.filter { it == showTime }.toPersistentList()
    )
}

@Composable
fun Seats(modifier: Modifier, io: SeatsIO, config: SeatsConfig) {

    val viewModel = rememberViewModel {
        SeatsViewModel(io, config)
    }

    val seats by viewModel.seats.collectAsState()
    val buttonEnabled by viewModel.buttonEnabled.collectAsState(false)

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item { Spacer(Modifier.height(32.dp)) }
        item {
            Text(
                text = "Your cinema: ",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight(500),
                )
            )
        }
        item { Spacer(Modifier.height(12.dp)) }

        item {
            CinemaRow(
                cinema = viewModel.cinema,
                selected = false,
                onItemClick = {}
            )
        }
        item { Spacer(Modifier.height(16.dp)) }
        item {
            Text(
                text = "Select seats: ",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight(500)
                )
            )
        }

        item { Spacer(Modifier.height(12.dp)) }

        item { SeatsGrid(items = seats, onItemClick = viewModel::onItemClick) }
        item { Spacer(Modifier.height(24.dp)) }

        item {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = viewModel::onNextClick,
                enabled = buttonEnabled
            ) {
                Text("Next")
            }
        }

        item { Spacer(Modifier.height(86.dp)) }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SeatsGrid(
    modifier: Modifier = Modifier,
    items: ImmutableList<Seat>,
    onItemClick: (Seat) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
    ) {
        FlowRow(
            modifier = modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            maxItemsInEachRow = SEATS_IN_A_ROW,
        ) {
            val itemModifier = Modifier
                .padding(4.dp)
                .weight(1f)
                .aspectRatio(1F)
                .clip(RoundedCornerShape(8.dp))

            items.forEach {
                Spacer(
                    modifier = itemModifier
                        .background(if (it.selected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.tertiary)
                        .clickable { onItemClick(it) })
            }
        }
    }
}