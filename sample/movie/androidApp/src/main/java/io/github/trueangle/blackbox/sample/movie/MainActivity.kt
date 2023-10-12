package io.github.trueangle.blackbox.sample.movie

import android.os.Bundle
import androidx.core.view.WindowCompat
import io.github.trueangle.blackbox.sample.movie.shared.CreateMovieApp
import io.github.trueangle.blackbox.sample.movie.shared.ui.AppConfig
import moe.tlaster.precompose.lifecycle.PreComposeActivity
import moe.tlaster.precompose.lifecycle.setContent

class MainActivity : PreComposeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        // You can add additional checks to filter out the deeplink data
        val action: String? = intent?.action
        val deeplinkUri = intent?.data?.toString()

        setContent {
            CreateMovieApp(AppConfig(deeplink = deeplinkUri))
        }
    }
}
