package io.github.trueangle.blackbox.sample.movie.desktop

import androidx.compose.ui.window.application
import io.github.trueangle.blackbox.sample.movie.shared.CreateMovieApp
import io.github.trueangle.blackbox.sample.movie.shared.ui.AppConfig
import moe.tlaster.precompose.PreComposeWindow

fun main() = application {
    PreComposeWindow(
        title = "Movie App",
        onCloseRequest = {
            exitApplication()
        },
    ) {
        CreateMovieApp(AppConfig())
    }
}