package com.github.trueangle.blackbox.sample.movie.shared.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
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

enum class HomeRoutes(val routePattern: String) {
    Featured("featured"),
    Trending("trending"),
    Orders("orders")
}

@Composable
internal fun Home(modifier: Modifier, dependencies: HomeDependencies, homeIO: HomeIO) {
    val homeScope = rememberScope { HomeScope(dependencies, homeIO) }

    Scaffold(
        modifier = modifier,
        bottomBar = { HomeBottomBar(homeScope.coordinator as HomeCoordinator) }
    ) {
        NavigationFlow(
            modifier = Modifier
                .padding(PaddingValues(bottom = it.calculateBottomPadding()))
                .fillMaxSize(),
            startDestination = HomeRoutes.Featured.routePattern,
            coordinator = homeScope.coordinator,
            persistNavState = true
        ) {
            scene(route = HomeRoutes.Featured.routePattern) {
                Featured(
                    modifier = Modifier,
                    dependencies = homeScope.featuredModuleDependencies,
                    io = homeScope.featuredIO
                )
            }

            scene(route = HomeRoutes.Trending.routePattern) {
                Trending(
                    modifier = Modifier,
                    dependencies = homeScope.trendingDependencies,
                    trendingIO = homeScope.trendingIO
                )
            }

            scene(route = HomeRoutes.Orders.routePattern) {
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
            icon = { Icon(imageVector = Icons.Outlined.Star, contentDescription = null) },
            selected = route == HomeRoutes.Featured.routePattern,
            onClick = {
                if (route != HomeRoutes.Featured.routePattern) {
                    coordinator.onBottomNavActionClick(HomeRoutes.Featured)
                }
            },
            label = { Text(text = "Featured") },
        )
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Outlined.List, contentDescription = null) },
            selected = route == HomeRoutes.Trending.routePattern,
            onClick = {
                if (route != HomeRoutes.Trending.routePattern) {
                    coordinator.onBottomNavActionClick(HomeRoutes.Trending)
                }
            },
            label = { Text(text = "Trending") }
        )
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Outlined.ShoppingCart, contentDescription = null) },
            selected = route == HomeRoutes.Orders.routePattern,
            onClick = {
                if (route != HomeRoutes.Orders.routePattern) {
                    coordinator.onBottomNavActionClick(HomeRoutes.Orders)
                }
            },
            label = { Text(text = "Orders") }
        )
    }
}