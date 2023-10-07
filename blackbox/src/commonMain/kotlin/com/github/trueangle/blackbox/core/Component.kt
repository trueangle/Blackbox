package com.github.trueangle.blackbox.core

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.github.trueangle.blackbox.multiplatform.Coordinator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow

@Stable
open class IO<Input, Output> {
    private val _inputFlow: MutableSharedFlow<Input> = MutableSharedFlow()
    private val _outputFlow: MutableSharedFlow<Output> = MutableSharedFlow()

    val input: kotlinx.coroutines.flow.Flow<Input> get() = _inputFlow
    val output: kotlinx.coroutines.flow.Flow<Output> get() = _outputFlow

    suspend fun input(event: Input) {
        _inputFlow.emit(event)
    }

    suspend fun output(event: Output) {
        _outputFlow.emit(event)
    }
}

@Immutable
abstract class BaseCoordinator {
    protected val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    abstract val navigator: Navigator

    fun onDestroy() {
        coroutineScope.cancel()
    }
}

interface Navigator {
    val canGoBack: Boolean

    val currentRoute: StateFlow<String?>

    fun navigateTo(route: String, routeOptions: RouteOptions? = null)

    fun back()
}

data class RouteOptions(
    val launchSingleTop: Boolean = false,
    val popUpTo: PopUpTo? = null
) {
    data class PopUpTo(
        val route: String,
        val inclusive: Boolean = false
    )
}

@OptIn(ExperimentalStdlibApi::class)
@Immutable
abstract class ViewScope : AutoCloseable {

    open fun onDestroy() {}

    override fun close() {
        onDestroy()
    }
}
