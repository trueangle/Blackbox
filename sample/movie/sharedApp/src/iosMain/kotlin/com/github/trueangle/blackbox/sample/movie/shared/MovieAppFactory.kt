package com.github.trueangle.blackbox.sample.movie.shared

import com.github.trueangle.blackbox.sample.movie.shared.ui.App
import com.github.trueangle.blackbox.sample.movie.shared.ui.AppDependencies
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.cinterop.BetaInteropApi
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import moe.tlaster.precompose.PreComposeAppController
import platform.Foundation.NSUserActivity

@OptIn(BetaInteropApi::class)
fun createMovieAppController(nsUserActivity: NSUserActivity?) =
    PreComposeAppController(nsUserActivity = nsUserActivity) {
        App(AppDependencies(KtorClient()))
    }

@OptIn(ExperimentalSerializationApi::class)
private fun KtorClient() = HttpClient(Darwin) {
    expectSuccess = true

    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.BODY
    }

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            useAlternativeNames = false
            explicitNulls = false
        })

        engine {
            configureRequest {
                setAllowsCellularAccess(true)

            }
        }
    }
}