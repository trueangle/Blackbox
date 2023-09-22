package com.github.trueangle.blackbox.sample.movie.shared.domain.model

data class Movie(
    val id: String,
    var title: String?,
    val name: String?,
    val poster_path: String?,
    val backdrop_path: String?,
    val release_date: String?,
    val vote_average: Double?,
    val genre_ids: List<Int>?,
    val overview: String,
    val adult: Boolean?,
    var tagline: String?,
    val budget: Double?,
    val revenue: Double?,
    val runtime: Int?,
    val homepage: String?,
    val status: String?,
    var addedTime: Long?,
    var dominantRgb: Int = 0
)

data class MovieDetails(
    val adult: Boolean,
    val backdropPath: String?,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String?,
    val id: Long,
    val imdbId: String?,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String?,
    val popularity: Double,
    val posterPath: String?,
    val productionCompanies: List<String>,
    val productionCountries: List<String>,
    val releaseDate: String?,
    val revenue: Long,
    val runtime: Int?,
    val spokenLanguages: List<String>,
    val status: String,
    val tagline: String?,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int
)