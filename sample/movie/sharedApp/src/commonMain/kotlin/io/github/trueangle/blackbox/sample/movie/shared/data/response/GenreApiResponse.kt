package io.github.trueangle.blackbox.sample.movie.shared.data.response

import io.github.trueangle.blackbox.sample.movie.shared.domain.model.Genre
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GenreResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)

@Serializable
internal data class GenreListResponse(
    @SerialName("genres")
    val genres: List<GenreResponse>,
)


internal fun GenreResponse.toDomainModel() = Genre(id, name)
internal fun GenreListResponse.toDomainModel() = genres.map { it.toDomainModel() }