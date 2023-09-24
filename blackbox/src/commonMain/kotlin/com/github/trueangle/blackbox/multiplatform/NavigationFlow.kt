package com.github.trueangle.blackbox.multiplatform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.RouteBuilder

@Composable
inline fun NavigationFlow(
    modifier: Modifier,
    coordinator: Coordinator,
    startDestination: String,
    persistNavState: Boolean = false,
    noinline builder: RouteBuilder.() -> Unit,
) {
    NavHost(
        modifier = modifier,
        navigator = coordinator.adapter.precomposeNavigator,
        initialRoute = startDestination,
        builder = builder,
        persistNavState = persistNavState
        //swipeProperties = SwipeProperties()
    )
}
