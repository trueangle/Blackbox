package com.github.trueangle.blackbox.sample.movie.ticketing.data.repository

import com.github.trueangle.blackbox.sample.movie.ticketing.domain.model.Cinema
import com.github.trueangle.blackbox.sample.movie.ticketing.domain.model.ShowTime
import com.github.trueangle.blackbox.sample.movie.ticketing.domain.repository.CinemaRepository
import kotlinx.collections.immutable.persistentListOf

class CinemaRepositoryImpl : CinemaRepository {
    override fun getAll() = MockedCinemas
}

private val MockedCinemas = persistentListOf(
    Cinema(
        id = 1,
        name = "Cineplex Odeon Los Angeles",
        address = "123 Main St",
        city = "Los Angeles",
        showTimes = persistentListOf(
            ShowTime(1, "10:00 AM", "12:00 PM", 10.0),
            ShowTime(2, "02:00 PM", "04:00 PM", 12.0),
            ShowTime(3, "06:00 PM", "08:00 PM", 15.0)
        )
    ),
    Cinema(
        id = 2,
        name = "AMC Theatres New York",
        address = "456 Elm St",
        city = "New York",
        showTimes = persistentListOf(
            ShowTime(4, "11:00 AM", "01:00 PM", 11.0),
            ShowTime(5, "03:00 PM", "05:00 PM", 13.0),
            ShowTime(6, "07:00 PM", "09:00 PM", 16.0)
        )
    ),
    Cinema(
        id = 3,
        name = "Regal Cinemas Chicago",
        address = "789 Oak Ave",
        city = "Chicago",
        showTimes = persistentListOf(
            ShowTime(7, "09:30 AM", "11:30 AM", 9.0),
            ShowTime(8, "01:30 PM", "03:30 PM", 11.0),
            ShowTime(9, "05:30 PM", "07:30 PM", 14.0)
        )
    ),
    Cinema(
        id = 4,
        name = "Cineworld London",
        address = "101 Pine Rd",
        city = "London",
        showTimes = persistentListOf(
            ShowTime(10, "10:30 AM", "12:30 PM", 10.5),
            ShowTime(11, "02:30 PM", "04:30 PM", 12.5),
            ShowTime(12, "06:30 PM", "08:30 PM", 15.5)
        )
    ),
    Cinema(
        id = 5,
        name = "Vue Cinemas Paris",
        address = "234 Maple St",
        city = "Paris",
        showTimes = persistentListOf(
            ShowTime(13, "11:15 AM", "01:15 PM", 11.5),
            ShowTime(14, "03:15 PM", "05:15 PM", 13.5),
            ShowTime(15, "07:15 PM", "09:15 PM", 16.5)
        )
    ),
    Cinema(
        id = 6,
        name = "Golden Screen Cinemas Kuala Lumpur",
        address = "567 Cedar Ave",
        city = "Kuala Lumpur",
        showTimes = persistentListOf(
            ShowTime(16, "09:45 AM", "11:45 AM", 9.5),
            ShowTime(17, "01:45 PM", "03:45 PM", 11.5),
            ShowTime(18, "05:45 PM", "07:45 PM", 14.5)
        )
    ),
    Cinema(
        id = 7,
        name = "Cinepolis Mexico City",
        address = "890 Birch Rd",
        city = "Mexico City",
        showTimes = persistentListOf(
            ShowTime(19, "10:15 AM", "12:15 PM", 10.25),
            ShowTime(20, "02:15 PM", "04:15 PM", 12.25),
            ShowTime(21, "06:15 PM", "08:15 PM", 15.25)
        )
    ),
    Cinema(
        id = 8,
        name = "Hoyts Cinemas Sydney",
        address = "111 Oakwood Dr",
        city = "Sydney",
        showTimes = persistentListOf(
            ShowTime(22, "11:30 AM", "01:30 PM", 11.75),
            ShowTime(23, "03:30 PM", "05:30 PM", 13.75),
            ShowTime(24, "07:30 PM", "09:30 PM", 16.75)
        )
    ),
    Cinema(
        id = 9,
        name = "Odeon Cinemas Manchester",
        address = "222 Birchwood Ln",
        city = "Manchester",
        showTimes = persistentListOf(
            ShowTime(25, "09:00 AM", "11:00 AM", 9.75),
            ShowTime(26, "01:00 PM", "03:00 PM", 11.75),
            ShowTime(27, "05:00 PM", "07:00 PM", 14.75)
        )
    ),
    Cinema(
        id = 10,
        name = "PVR Cinemas Mumbai",
        address = "333 Elmwood Dr",
        city = "Mumbai",
        showTimes = persistentListOf(
            ShowTime(28, "10:45 AM", "12:45 PM", 10.125),
            ShowTime(29, "02:45 PM", "04:45 PM", 12.125),
            ShowTime(30, "06:45 PM", "08:45 PM", 15.125)
        )
    )
)
