package com.github.trueangle.blackbox.sample.movie.ticketing.domain.model

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList

@Stable
data class Order(
    val movieName: String,
    val cinema: Cinema,
    val seats: ImmutableList<Seat>,
    val showTime: ShowTime
)