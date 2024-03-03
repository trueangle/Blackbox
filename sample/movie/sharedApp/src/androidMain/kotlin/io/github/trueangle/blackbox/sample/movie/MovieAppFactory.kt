package io.github.trueangle.blackbox.sample.movie

import androidx.compose.runtime.Composable
import io.github.trueangle.blackbox.sample.movie.shared.ui.App
import io.github.trueangle.blackbox.sample.movie.shared.ui.AppConfig
import io.github.trueangle.blackbox.sample.movie.shared.ui.AppDependencies
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import moe.tlaster.precompose.PreComposeApp

@Composable
fun CreateMovieApp(config: AppConfig) = PreComposeApp { App(AppDependencies(KtorClient()), config) }

@OptIn(ExperimentalSerializationApi::class)
private fun KtorClient() = HttpClient {
    expectSuccess = true

    install(Logging) {
        logger = Logger.ANDROID
        level = LogLevel.BODY
    }

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            useAlternativeNames = false
            explicitNulls = false
        })
    }
}