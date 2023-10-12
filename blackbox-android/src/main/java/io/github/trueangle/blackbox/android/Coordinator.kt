package io.github.trueangle.blackbox.android

import io.github.trueangle.blackbox.core.BaseCoordinator
import io.github.trueangle.blackbox.core.Navigator
import io.github.trueangle.blackbox.core.RouteOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

class CommandNavigator(private val coroutineScope: CoroutineScope) : Navigator {
    private val currentRoutes = MutableStateFlow<List<String>>(mutableListOf())

    private val channel = Channel<Command>(Channel.CONFLATED)

    val commands = channel.receiveAsFlow()

    override val canGoBack: Boolean get() = currentRoutes.value.size > 1

    override val currentRoute: StateFlow<String?> = currentRoutes.map { it.lastOrNull() }.stateIn(
        coroutineScope,
        SharingStarted.Eagerly,
        currentRoutes.value.lastOrNull()
    )

    override fun navigateTo(route: String, routeOptions: RouteOptions?) {
        val result = channel.trySend(Command.Navigate(route, routeOptions))

        if (result.isSuccess) {
            val routes = currentRoutes.value.toMutableList()
            routes.add(route)
            currentRoutes.value = routes
        }
    }

    override fun back() {
        val result = channel.trySend(Command.Back)
        if (result.isSuccess) {
            val routes = currentRoutes.value.toMutableList()
            routes.removeLastOrNull()
            currentRoutes.value = routes
        }
    }

    sealed interface Command {
        data object Back : Command

        data class Navigate(val route: String, val routeOptions: RouteOptions? = null) : Command
    }
}

abstract class Coordinator : BaseCoordinator() {
    override val navigator: CommandNavigator = CommandNavigator(coroutineScope)
    val commands = navigator.commands
}

