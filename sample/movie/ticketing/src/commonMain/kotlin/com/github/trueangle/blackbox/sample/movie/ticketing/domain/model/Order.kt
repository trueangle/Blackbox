package com.github.trueangle.blackbox.sample.movie.ticketing.domain.model

import kotlinx.collections.immutable.ImmutableList

data class Order(
    val movieName: String,
    val cinema: Cinema,
    val seats: ImmutableList<Seat>,
    val showTime: ShowTime
)