package com.github.trueangle.blackbox.sample.movie.shared.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.github.trueangle.blackbox.multiplatform.NavigationFlow
import com.github.trueangle.blackbox.multiplatform.rememberScope
import com.github.trueangle.blackbox.sample.movie.shared.ui.featured.Featured
import com.github.trueangle.blackbox.sample.movie.shared.ui.trending.Trending

internal sealed interface HomeRoutes {
    enum class BottomBar(val RoutePattern: String) : HomeRoutes {
        Featured("featured"),
        Trending("trending"),
        Orders("orders")
    }
}

@Composable
internal fun Home(modifier: Modifier, dependencies: HomeDependencies, homeIO: HomeIO) {
    val homeScope = rememberScope(HomeScope::class) { HomeScope(dependencies, homeIO) }

    Scaffold(
        modifier = modifier,
        bottomBar = { HomeBottomBar(homeScope.coordinator as HomeCoordinator) }
    ) {
        NavigationFlow(
            modifier = Modifier.fillMaxSize(),
            startDestination = HomeRoutes.BottomBar.Featured.RoutePattern,
            coordinator = homeScope.coordinator,
            persistNavState = true
        ) {
            scene(route = HomeRoutes.BottomBar.Featured.RoutePattern) {
                Featured(
                    modifier = Modifier,
                    dependencies = homeScope.featuredModuleDependencies,
                    io = homeScope.featuredIO
                )
            }

            scene(route = HomeRoutes.BottomBar.Trending.RoutePattern) {
                Trending(
                    modifier = Modifier,
                    dependencies = homeScope.trendingDependencies,
                    trendingIO = homeScope.trendingIO
                )
            }

            scene(route = HomeRoutes.BottomBar.Orders.RoutePattern) {
                homeScope.ticketingFactory.Orders(Modifier.fillMaxSize())
            }
        }
    }
}


@Composable
private fun HomeBottomBar(coordinator: HomeCoordinator) {
    val route by coordinator.currentRoute.collectAsState(null)

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Outlined.Home, contentDescription = null) },
            selected = route == HomeRoutes.BottomBar.Featured.RoutePattern,
            onClick = {
                coordinator.onBottomNavActionClick(HomeRoutes.BottomBar.Featured)
            },
            label = { Text(text = "Showing") },
        )
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Outlined.Person, contentDescription = null) },
            selected = route == HomeRoutes.BottomBar.Trending.RoutePattern,
            onClick = {
                coordinator.onBottomNavActionClick(HomeRoutes.BottomBar.Trending)
            },
            label = { Text(text = "Trending") }
        )
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Outlined.Add, contentDescription = null) },
            selected = route == HomeRoutes.BottomBar.Orders.RoutePattern,
            onClick = {
                coordinator.onBottomNavActionClick(HomeRoutes.BottomBar.Orders)
            },
            label = { Text(text = "Orders") }
        )
    }
}