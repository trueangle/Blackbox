package io.github.trueangle.blackbox.sample.movie.shared.data.repository

import io.github.trueangle.blackbox.sample.movie.shared.data.api.MoviesApi
import io.github.trueangle.blackbox.sample.movie.shared.data.response.toDomainModel
import io.github.trueangle.blackbox.sample.movie.shared.domain.model.Genre
import io.github.trueangle.blackbox.sample.movie.shared.domain.repository.GenreRepository

internal class GenreRepositoryImpl(private val api: MoviesApi) : GenreRepository {
    override suspend fun getGenres(): List<Genre> = api.getGenres().toDomainModel()
}