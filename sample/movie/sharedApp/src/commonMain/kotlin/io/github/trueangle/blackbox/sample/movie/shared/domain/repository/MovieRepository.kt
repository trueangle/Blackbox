package io.github.trueangle.blackbox.sample.movie.shared.domain.repository

import io.github.trueangle.blackbox.sample.movie.shared.domain.model.Movie
import io.github.trueangle.blackbox.sample.movie.shared.domain.model.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMyWatchList(): Flow<Set<Movie>>

    suspend fun getMovies(page: Int): List<Movie>
    suspend fun addToWatchList(movie: Movie)

    suspend fun getTrending(): List<Movie>
    suspend fun getPopular(): List<Movie>
    suspend fun getTopRated(): List<Movie>

    suspend fun getTopRatedTVShows(): List<Movie>
    suspend fun getTrendingTVShows(): List<Movie>
    suspend fun getMovieDetailsById(movieId: String): MovieDetails
    suspend fun getSimilar(movieId: String): List<Movie>
}