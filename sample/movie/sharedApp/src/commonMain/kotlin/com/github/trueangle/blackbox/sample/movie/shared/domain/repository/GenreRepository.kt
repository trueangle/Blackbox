package com.github.trueangle.blackbox.sample.movie.shared.domain.repository

import com.github.trueangle.blackbox.sample.movie.shared.domain.model.Genre

interface GenreRepository {
    suspend fun getGenres(): List<Genre>
}