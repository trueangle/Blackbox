package com.github.trueangle.blackbox.multiplatform

import com.github.trueangle.blackbox.core.BaseCoordinator
import com.github.trueangle.blackbox.core.Navigator
import com.github.trueangle.blackbox.core.RouteOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.BackStackEntry
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.PopUpTo

private typealias PrecomposeNavigator = moe.tlaster.precompose.navigation.Navigator

internal class NavigatorAdapter(
    private val precomposeNavigator: PrecomposeNavigator,
    coroutineScope: CoroutineScope
) : Navigator {

    private val canGoBackFlow = precomposeNavigator.canGoBack.stateIn(
        coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )

    override val canGoBack: Boolean get() = canGoBackFlow.value

    override val currentRoute: StateFlow<String?> =
        precomposeNavigator.currentEntry.map { it?.route?.route }.stateIn(
            coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    override fun navigateTo(route: String, routeOptions: RouteOptions?) {
        precomposeNavigator.navigate(route, routeOptions?.toNavOptions())
    }

    override fun back() {
        precomposeNavigator.goBack()
    }

    private fun RouteOptions.toNavOptions() = NavOptions(
        launchSingleTop = launchSingleTop,
        popUpTo = popUpTo?.let {
            PopUpTo(
                route = it.route,
                inclusive = it.inclusive
            )
        } ?: PopUpTo.None
    )
}

abstract class Coordinator : BaseCoordinator() {
    @PublishedApi
    internal val precomposeNavigator = PrecomposeNavigator()

    override val navigator: Navigator = NavigatorAdapter(precomposeNavigator, coroutineScope)

    val currentRoute = navigator.currentRoute
}

class BasicCoordinator : Coordinator()