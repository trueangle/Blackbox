package com.github.trueangle.blackbox.sample.movie.ticketing.ui.orders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.truangle.blackbox.design.typography
import com.github.trueangle.blackbox.multiplatform.ViewModel
import com.github.trueangle.blackbox.multiplatform.ViewModelScope
import com.github.trueangle.blackbox.multiplatform.rememberScope
import com.github.trueangle.blackbox.multiplatform.rememberViewModel
import com.github.trueangle.blackbox.sample.movie.design.TopAppBar
import com.github.trueangle.blackbox.sample.movie.ticketing.domain.model.Order
import com.github.trueangle.blackbox.sample.movie.ticketing.domain.repository.OrderRepository

@Immutable
class OrdersDependencies(val repository: OrderRepository)

private class OrdersViewModel(private val orderRepository: OrderRepository) : ViewModel() {
    val orders = orderRepository.orders()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Orders(modifier: Modifier, dependencies: OrdersDependencies) {
    val viewModel = rememberViewModel {
        OrdersViewModel(orderRepository = dependencies.repository)
    }

    val orders by viewModel.orders.collectAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar("My orders", scrollBehavior)
        }
    ) { pad ->
        if (orders.isEmpty()) {
            EmptyOrdersSection()
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = pad) {
                items(orders) {
                    OrderRow(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        order = it
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyOrdersSection() {
    Column {
        Spacer(modifier = Modifier.padding(100.dp))
        Text(
            text = "No orders yet",
            style = typography.titleLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Purchased tickets will appear here",
            style = typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}


@Composable
private fun OrderRow(
    modifier: Modifier,
    order: Order
) {
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
                    text = order.movieName,
                    fontWeight = FontWeight(500),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Text(
                modifier = Modifier,
                text = order.cinema.name + ", " + order.cinema.address,
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
                    order.seats.forEach {
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
                    text = order.showTime.timeRangeString,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}