package com.github.trueangle.blackbox.sample.movie.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.trueangle.blackbox.sample.movie.shared.CreateMovieApp
import moe.tlaster.precompose.PreComposeWindow

fun main() = application {
    /*   Window(onCloseRequest = ::exitApplication) {
           CreateMovieApp()
       }
   */
    PreComposeWindow(
        title = "Movie App",
        onCloseRequest = {
            exitApplication()
        },
    ) {
        CreateMovieApp()
    }
}