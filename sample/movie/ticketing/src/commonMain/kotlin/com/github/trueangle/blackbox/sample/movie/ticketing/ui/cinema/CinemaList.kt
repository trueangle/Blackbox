package com.github.trueangle.blackbox.sample.movie.ticketing.ui.cinema

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.trueangle.blackbox.core.IO
import com.github.trueangle.blackbox.multiplatform.ViewModel
import com.github.trueangle.blackbox.multiplatform.ViewModelScope
import com.github.trueangle.blackbox.multiplatform.rememberScope
import com.github.trueangle.blackbox.multiplatform.rememberViewModel
import com.github.trueangle.blackbox.sample.movie.ticketing.common.Heading
import com.github.trueangle.blackbox.sample.movie.ticketing.data.repository.CinemaRepositoryImpl
import com.github.trueangle.blackbox.sample.movie.ticketing.domain.model.Cinema
import com.github.trueangle.blackbox.sample.movie.ticketing.domain.model.ShowTime
import com.github.trueangle.blackbox.sample.movie.ticketing.domain.repository.CinemaRepository
import kotlinx.coroutines.launch

sealed interface CinemaListOutput {
    data class OnShowTimeSelected(val selectedCinema: Cinema, val selectedShowTime: ShowTime) :
        CinemaListOutput
}

class CinemaListIO : IO<Nothing, CinemaListOutput>()

@Stable
private class CinemaListViewModel(
    private val cinemaRepository: CinemaRepository,
    private val io: CinemaListIO
) : ViewModel() {
    val cinemaItems = mutableStateOf(cinemaRepository.getAll().map { CinemaItem(it) })

    fun onItemClick(item: CinemaItem, selectedShowTime: ShowTime) {
        coroutineScope.launch {
            io.output(
                CinemaListOutput.OnShowTimeSelected(
                    selectedCinema = item.cinema,
                    selectedShowTime = selectedShowTime
                )
            )
        }
    }
}

private data class CinemaItem(
    val cinema: Cinema,
    val selected: Boolean = false
)

@Composable
fun CinemaList(modifier: Modifier, movieName: String, io: CinemaListIO) {
    val viewModel = rememberViewModel(key = "CinemaListViewModelScope") {
        CinemaListViewModel(CinemaRepositoryImpl(), io)
    }

    val items by viewModel.cinemaItems

    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        item { Spacer(Modifier.height(32.dp)) }
        item { Heading("The list of available cinemas showing $movieName") }
        item { Spacer(Modifier.height(18.dp)) }

        items(items) { cinemaItem ->
            CinemaRow(
                modifier = Modifier.padding(bottom = 16.dp),
                cinema = cinemaItem.cinema,
                //imageResourceId = it.imageResourceId,
                selected = cinemaItem.selected,
                onItemClick = {
                    viewModel.onItemClick(item = cinemaItem, selectedShowTime = it)
                }
            )
        }

        item { Spacer(Modifier.height(86.dp)) }
    }
}

@Composable
fun CinemaRow(
    cinema: Cinema,
    selected: Boolean,
    onItemClick: (ShowTime) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.clip(MaterialTheme.shapes.small)
    ) {
        Column(modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = cinema.name,
                    fontWeight = FontWeight(500),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Text(
                modifier = modifier.padding(horizontal = 16.dp),
                text = cinema.address,
                style = MaterialTheme.typography.titleSmall,
            )

            LazyRow(contentPadding = PaddingValues(horizontal = 8.dp)) {
                items(cinema.showTimes) {
                    ShowTimeItem(it, onItemClick)
                }
            }
        }
    }
}

@Composable
fun ShowTimeItem(showTime: ShowTime, onItemClick: (ShowTime) -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = {
                onItemClick(showTime)
            }),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color.Transparent
        ),
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = showTime.startTime,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8F)
                ),
            )

            Text(
                text = "from $${showTime.price}",
                style = TextStyle(
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight(600)
                ),
            )
        }
    }
}