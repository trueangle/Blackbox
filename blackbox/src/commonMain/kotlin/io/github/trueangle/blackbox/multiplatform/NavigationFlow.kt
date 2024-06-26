package io.github.trueangle.blackbox.multiplatform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.RouteBuilder

@Composable
inline fun NavigationFlow(
    modifier: Modifier,
    coordinator: Coordinator,
    startDestination: String,
    noinline builder: RouteBuilder.() -> Unit,
) {
    NavHost(
        modifier = modifier,
        navigator = coordinator.precomposeNavigator,
        initialRoute = startDestination,
        builder = builder
    )
}
