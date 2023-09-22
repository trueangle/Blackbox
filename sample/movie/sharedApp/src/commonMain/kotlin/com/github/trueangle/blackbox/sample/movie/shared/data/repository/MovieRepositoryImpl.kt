package com.github.trueangle.blackbox.sample.movie.shared.data.repository

import com.github.trueangle.blackbox.sample.movie.shared.data.api.MoviesApi
import com.github.truangle.blackbox.movieapp.data.response.toDomainModel
import com.github.trueangle.blackbox.sample.movie.shared.domain.model.Movie
import com.github.trueangle.blackbox.sample.movie.shared.domain.model.MovieDetails
import com.github.trueangle.blackbox.sample.movie.shared.domain.repository.MovieRepository
import kotlinx.collections.immutable.persistentHashSetOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class MovieRepositoryImpl(private val api: MoviesApi) : MovieRepository {
    private val watchList = MutableStateFlow(persistentHashSetOf<Movie>())

    override fun getMyWatchList(): Flow<Set<Movie>> = watchList

    override suspend fun getMovies(page: Int): List<Movie> =
        api
            .getPerPage(page)
            .toDomainModel()

    override suspend fun addToWatchList(movie: Movie) {
        val movies = watchList.value
        watchList.value = movies.add(movie)
    }

    override suspend fun getTrending(): List<Movie> = api.getTrending().toDomainModel()

    override suspend fun getPopular(): List<Movie> = api.getPopular().toDomainModel()

    override suspend fun getTopRated(): List<Movie> = api.getTopRated().toDomainModel()

    override suspend fun getTopRatedTVShows(): List<Movie> =
        api.getTopRatedTVShows().toDomainModel()

    override suspend fun getTrendingTVShows(): List<Movie> =
        api.getTrendingTVShows().toDomainModel()

    override suspend fun getMovieDetailsById(movieId: String): MovieDetails =
        api.getMovieDetailsById(movieId).toDomainModel()

    override suspend fun getSimilar(movieId: String): List<Movie> =
        api.getSimilarMovies(movieId).toDomainModel()
}