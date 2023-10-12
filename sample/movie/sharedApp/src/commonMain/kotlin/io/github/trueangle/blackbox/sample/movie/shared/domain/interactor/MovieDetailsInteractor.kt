package io.github.trueangle.blackbox.sample.movie.shared.domain.interactor

import io.github.trueangle.blackbox.sample.movie.shared.domain.model.Genre
import io.github.trueangle.blackbox.sample.movie.shared.domain.model.Movie
import io.github.trueangle.blackbox.sample.movie.shared.domain.model.MovieDetails
import io.github.trueangle.blackbox.sample.movie.shared.domain.repository.GenreRepository
import io.github.trueangle.blackbox.sample.movie.shared.domain.repository.MovieRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

data class MovieDetailsResult(
    val movie: MovieDetails? = null,
    val genres: ImmutableList<Genre> = persistentListOf(),
    val similarMovies: ImmutableList<Movie> = persistentListOf()
)

interface MovieDetailsInteractor {
    suspend fun getDetailsByMovieId(movieId: String): MovieDetailsResult
}

internal class MovieDetailsInteractorImpl(
    private val movieRepository: MovieRepository,
    private val genreRepository: GenreRepository
) : MovieDetailsInteractor {
    override suspend fun getDetailsByMovieId(movieId: String): MovieDetailsResult = coroutineScope {
        val movie = async { movieRepository.getMovieDetailsById(movieId) }
        val genres = async { genreRepository.getGenres() }
        val similarMovies = async { movieRepository.getSimilar(movieId) }

        MovieDetailsResult(
            movie = movie.await(),
            genres = genres.await().toPersistentList(),
            similarMovies = similarMovies.await().toPersistentList()
        )
    }
}