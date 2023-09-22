package com.github.trueangle.blackbox.sample.movie.ticketing.domain.model

data class Cinema(
    val id: Long,
    val name: String,
    val address: String,
    val city: String,
    val showTimes: List<ShowTime>
)

data class ShowTime(
    val id: Long,
    val startTime: String,
    val endTime: String,
    val price: Double,
) {
    val timeRangeString = "$startTime - $endTime"
}
