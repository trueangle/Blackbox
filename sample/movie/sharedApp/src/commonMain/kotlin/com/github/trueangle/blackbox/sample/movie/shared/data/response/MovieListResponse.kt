package com.github.truangle.blackbox.movieapp.data.response

import com.github.trueangle.blackbox.sample.movie.shared.domain.model.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MovieResponse(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String?,
    @SerialName("name") val name: String?,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("release_date") val releaseDate: String?,
    @SerialName("vote_average") val voteAverage: Double?,
    @SerialName("genre_ids") val genreIds: List<Int>?,
    @SerialName("overview") val overview: String,
    @SerialName("adult") val adult: Boolean?,
    @SerialName("tagline") var tagline: String?,
    @SerialName("budget") val budget: Double?,
    @SerialName("revenue") val revenue: Double?,
    @SerialName("runtime") val runtime: Int?,
    @SerialName("homepage") val homepage: String?,
    @SerialName("status") val status: String?,
    @SerialName("added_time") var addedTime: Long?,
    @SerialName("dominant_rgb") var dominantRgb: Int = 0
)

@Serializable
internal data class MovieListResponse(
    @SerialName("page") val pages: Int,
    @SerialName("results") val movies: List<MovieResponse>
)

internal fun MovieListResponse.toDomainModel() = movies.map { it.toDomainModel() }

internal fun MovieResponse.toDomainModel() = Movie(
    id = id.toString(),
    title = title,
    name = name,
    poster_path = posterPath,
    backdrop_path = backdropPath,
    release_date = releaseDate,
    vote_average = voteAverage,
    genre_ids = genreIds,
    overview = overview,
    adult = adult,
    tagline = tagline,
    budget = budget,
    revenue = revenue,
    runtime = runtime,
    homepage = homepage,
    status = status,
    addedTime = addedTime,
    dominantRgb = dominantRgb
)
