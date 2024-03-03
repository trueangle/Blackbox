package io.github.trueangle.blackbox.sample.movie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.view.WindowCompat
import androidx.activity.compose.setContent
import io.github.trueangle.blackbox.sample.movie.shared.ui.AppConfig

class MainActivity : ComponentActivity() {
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
