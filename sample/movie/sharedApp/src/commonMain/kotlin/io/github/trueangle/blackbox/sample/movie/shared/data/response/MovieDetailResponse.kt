package io.github.trueangle.blackbox.sample.movie.shared.data.response

import io.github.trueangle.blackbox.sample.movie.shared.domain.model.MovieDetails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MovieDetailsResponse(
    @SerialName("adult")
    val isAdult: Boolean,

    @SerialName("backdrop_path")
    val backdropPath: String?,

    @SerialName("budget")
    val budget: Int,

    @SerialName("genres")
    val genres: List<GenreResponse>,

    @SerialName("homepage")
    val homepage: String?,

    @SerialName("id")
    val id: Long,

    @SerialName("imdb_id")
    val imdbId: String?,

    @SerialName("original_language")
    val originalLanguage: String,

    @SerialName("original_title")
    val originalTitle: String,

    @SerialName("overview")
    val overview: String?,

    @SerialName("popularity")
    val popularity: Double,

    @SerialName("poster_path")
    val posterPath: String?,

    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompanyResponse>,

    @SerialName("production_countries")
    val productionCountries: List<ProductionCountryResponse>,

    @SerialName("release_date")
    val releaseDate: String?,

    @SerialName("revenue")
    val revenue: Long,

    @SerialName("runtime")
    val runtime: Int?,

    @SerialName("spoken_languages")
    val spokenLanguages: List<SpokenLanguageResponse>,

    @SerialName("status")
    val status: String,

    @SerialName("tagline")
    val tagline: String?,

    @SerialName("title")
    val title: String,

    @SerialName("video")
    val isVideo: Boolean,

    @SerialName("vote_average")
    val voteAverage: Double,

    @SerialName("vote_count")
    val voteCount: Int
)

@Serializable
data class ProductionCompanyResponse(
    @SerialName("id")
    val id: Int,

    @SerialName("logo_path")
    val logoPath: String?,

    @SerialName("name")
    val name: String,

    @SerialName("origin_country")
    val originCountry: String
)

@Serializable
data class ProductionCountryResponse(
    @SerialName("iso_3166_1")
    val iso31661: String,

    @SerialName("name")
    val name: String
)

@Serializable
data class SpokenLanguageResponse(
    @SerialName("english_name")
    val englishName: String,

    @SerialName("iso_639_1")
    val iso6391: String,

    @SerialName("name")
    val name: String
)


internal fun MovieDetailsResponse.toDomainModel(): MovieDetails {
    return MovieDetails(
        adult = isAdult,
        backdropPath = backdropPath,
        budget = budget,
        genres = genres.map { it.toDomainModel() },
        homepage = homepage,
        id = id,
        imdbId = imdbId,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        productionCompanies = productionCompanies.map { it.name },
        productionCountries = productionCountries.map { it.name },
        releaseDate = releaseDate,
        revenue = revenue,
        runtime = runtime,
        spokenLanguages = spokenLanguages.map { it.name },
        status = status,
        tagline = tagline,
        title = title,
        video = isVideo,
        voteAverage = voteAverage,
        voteCount = voteCount
    )
}