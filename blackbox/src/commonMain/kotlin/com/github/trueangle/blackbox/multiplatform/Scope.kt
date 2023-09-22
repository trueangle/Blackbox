package com.github.trueangle.blackbox.multiplatform

import androidx.compose.runtime.Composable
import com.github.trueangle.blackbox.core.ViewScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import moe.tlaster.precompose.stateholder.LocalStateHolder

open class FlowScope : ViewScope() {
    private var _coordinator: Coordinator? = null

    val coordinator get() = requireNotNull(_coordinator)

    fun coordinator(creator: () -> Coordinator) {
        _coordinator = creator()
    }

    override fun onDestroy() {
        _coordinator?.onDestroy()
    }
}

// todo make a generic rememberScope for storing Any data
// cannot use T::class
// https://github.com/JetBrains/compose-multiplatform/issues/3147
@Composable
fun <T : ViewScope> rememberScope(
    key: String, // todo think of unique key for each scope class
    scopeCreator: () -> T
): T {

    // todo proper error description
    val stateHolder = checkNotNull(LocalStateHolder.current) {
        "Require LocalStateHolder not null"
    }

    return stateHolder.getOrPut(key) {
        scopeCreator()
    }
}

@Composable
fun <T : Coordinator> rememberCoordinator(key: String, creator: () -> T): T = rememberScope(key) {
    FlowScope().apply { coordinator { creator() } }
}.coordinator as T

// Predefined scopes
abstract class ViewModel {
    protected val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    open fun onDestroy() {
        coroutineScope.cancel()
    }
}

class ViewModelScope(val onCreate: () -> ViewModel) : ViewScope() {

    private val _viewModel = lazy { onCreate() }
    val viewModel by _viewModel

    override fun onDestroy() {
        if (_viewModel.isInitialized()) {
            viewModel.onDestroy()
        }
    }
}