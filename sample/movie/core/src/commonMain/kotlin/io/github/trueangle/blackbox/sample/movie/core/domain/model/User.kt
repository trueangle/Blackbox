package io.github.trueangle.blackbox.sample.movie.core.domain.model

import androidx.compose.runtime.Stable

@Stable
data class User(
    val id: Long,
    val name: String,
    val email: String,
    val phone: String
)