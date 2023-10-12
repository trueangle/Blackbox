package io.github.trueangle.blackbox.sample.movie.shared.domain.repository

import io.github.trueangle.blackbox.sample.movie.shared.domain.model.Genre

interface GenreRepository {
    suspend fun getGenres(): List<Genre>
}