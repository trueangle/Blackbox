package com.github.trueangle.blackbox.android

import com.github.trueangle.blackbox.core.BaseCoordinator
import com.github.trueangle.blackbox.core.Navigator
import com.github.trueangle.blackbox.core.RouteOptions
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class CommandNavigator : Navigator {

    private val channel = Channel<Command>(Channel.CONFLATED)

    val commands = channel.receiveAsFlow()

    override fun navigateTo(route: String, routeOptions: RouteOptions?) {
        channel.trySend(Command.Navigate(route, routeOptions))
    }

    override fun back() {
        channel.trySend(Command.Back)
    }

    sealed interface Command {
        data object Back : Command

        data class Navigate(val route: String, val routeOptions: RouteOptions? = null) : Command
    }
}

abstract class Coordinator : BaseCoordinator() {
    override val navigator: CommandNavigator = CommandNavigator()
    val commands = navigator.commands
}

