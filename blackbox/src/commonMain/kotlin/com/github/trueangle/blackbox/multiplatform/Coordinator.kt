package com.github.trueangle.blackbox.multiplatform

import com.github.trueangle.blackbox.core.BaseCoordinator
import com.github.trueangle.blackbox.core.Navigator
import com.github.trueangle.blackbox.core.RouteOptions
import kotlinx.coroutines.flow.map
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.PopUpTo

private typealias PrecomposeNavigator = moe.tlaster.precompose.navigation.Navigator

// workaround adapter since Precompose Navigator is final class and cannot implement Navigator interface
class NavigatorAdapter(val precomposeNavigator: PrecomposeNavigator) : Navigator {

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
    val adapter = NavigatorAdapter(PrecomposeNavigator())
    override val navigator: Navigator = adapter

    val currentRoute = adapter.precomposeNavigator.currentEntry.map { it?.route?.route }
}

class BasicCoordinator : Coordinator()