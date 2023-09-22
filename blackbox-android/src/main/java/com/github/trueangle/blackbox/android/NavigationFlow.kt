package com.github.trueangle.blackbox.android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationFlow(
    modifier: Modifier,
    coordinator: Coordinator,
    startDestination: String,
    builder: NavGraphBuilder.() -> Unit,
) {
    val navController = rememberNavController()

    // todo map route options
    LaunchedEffect(Unit) {
        coordinator.commands.collect {
            when (it) {
                CommandNavigator.Command.Back -> navController.popBackStack()
                is CommandNavigator.Command.Navigate -> navController.navigate(it.route)
            }
        }
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        builder = builder
    )
}