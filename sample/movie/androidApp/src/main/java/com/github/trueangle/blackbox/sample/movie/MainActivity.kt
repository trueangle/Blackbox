package com.github.trueangle.blackbox.sample.movie

import android.os.Bundle
import androidx.core.view.WindowCompat
import com.github.trueangle.blackbox.sample.movie.shared.CreateMovieApp
import moe.tlaster.precompose.lifecycle.PreComposeActivity
import moe.tlaster.precompose.lifecycle.setContent

class MainActivity : PreComposeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            CreateMovieApp()
        }
    }
}
