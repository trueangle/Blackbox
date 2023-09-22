package com.github.trueangle.blackbox.sample.movie.shared.data.repository

import com.github.trueangle.blackbox.sample.movie.shared.data.api.MoviesApi
import com.github.truangle.blackbox.movieapp.data.response.toDomainModel
import com.github.trueangle.blackbox.sample.movie.shared.domain.model.Genre
import com.github.trueangle.blackbox.sample.movie.shared.domain.repository.GenreRepository

internal class GenreRepositoryImpl(private val api: MoviesApi) : GenreRepository {
    override suspend fun getGenres(): List<Genre> = api.getGenres().toDomainModel()
}