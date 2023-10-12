package io.github.trueangle.blackbox.sample.movie.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import io.github.trueangle.blackbox.core.IO
import io.github.trueangle.blackbox.sample.movie.core.domain.model.User
import io.github.trueangle.blackbox.core.RouteOptions
import io.github.trueangle.blackbox.multiplatform.NavigationFlow
import io.github.trueangle.blackbox.multiplatform.rememberCoordinator
import io.github.trueangle.blackbox.multiplatform.BasicCoordinator
import kotlinx.coroutines.launch

private sealed interface AuthRoutes {
    data object SignIn : AuthRoutes {
        const val RoutePattern: String = "auth/signIn"
    }

    data object Profile : AuthRoutes {
        const val RoutePattern: String = "auth/profile"
    }
}

sealed interface AuthFlowOutput {
    data class OnPickUserData(val user: User) : AuthFlowOutput
    data object OnClose : AuthFlowOutput
}

class AuthIO : IO<Nothing, AuthFlowOutput>()

@Composable
fun AuthFlow(modifier: Modifier, io: AuthIO) {
    val coordinator = rememberCoordinator(key = "AuthFlowCoordinator") { BasicCoordinator() }
    val coroutineScope = rememberCoroutineScope()

    NavigationFlow(
        modifier = modifier,
        coordinator = coordinator,
        startDestination = AuthRoutes.SignIn.RoutePattern
    ) {
        scene(route = AuthRoutes.SignIn.RoutePattern) {
            SignIn(
                onSubmit = {
                    coordinator.navigator.navigateTo(
                        route = AuthRoutes.Profile.RoutePattern,
                        routeOptions = RouteOptions(
                            popUpTo = RouteOptions.PopUpTo(AuthRoutes.SignIn.RoutePattern, true)
                        )
                    )
                },
                onClose = {
                    coroutineScope.launch { io.output(AuthFlowOutput.OnClose) }
                }
            )
        }

        scene(route = AuthRoutes.Profile.RoutePattern) {
            Profile(
                modifier = Modifier,
                onClose = {
                    coroutineScope.launch { io.output(AuthFlowOutput.OnClose) }
                },
                onPick = {
                    coroutineScope.launch { io.output(AuthFlowOutput.OnPickUserData(it)) }
                }
            )
        }
    }
}