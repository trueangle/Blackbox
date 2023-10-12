package io.github.trueangle.blackbox.sample.movie.ticketing.domain.model

import androidx.compose.runtime.Stable

@Stable
data class Seat(val row: Int, val seat: Int, val selected: Boolean)