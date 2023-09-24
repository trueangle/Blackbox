package com.github.trueangle.blackbox.multiplatform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.github.trueangle.blackbox.core.ViewScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import moe.tlaster.precompose.stateholder.LocalStateHolder
import kotlin.reflect.KClass

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

@Composable
fun <T : ViewScope> rememberScope(
    kClass: KClass<T>? = null,
    key: String? = null,
    scopeCreator: () -> T
): T {

    // cannot use reified T::class name as a key on iOS, use explicit kClass param to workaround this
    // https://github.com/JetBrains/compose-multiplatform/issues/3147
    val scopeKey = checkNotNull(key ?: kClass?.qualifiedName) {
        "You must specify either class or key to uniquely identify the scope"
    }

    val stateHolder = checkNotNull(LocalStateHolder.current) { "LocalStateHolder must not be null" }

    return stateHolder.getOrPut(scopeKey) {
        scopeCreator()
    }
}

@Composable
inline fun <T : Coordinator> rememberCoordinator(
    kClass: KClass<T>? = null,
    key: String? = null,
    crossinline creator: () -> T
): T {
    val scopeKey = checkNotNull(key ?: kClass?.qualifiedName) {
        "You must specify either class or key to uniquely identify the Coordinator"
    }

    return rememberScope(key = scopeKey) {
        FlowScope().apply { coordinator { creator() } }
    }.coordinator as T
}

@Composable
inline fun <T : ViewModel> rememberViewModel(
    kClass: KClass<T>? = null,
    key: String? = null,
    crossinline creator: () -> T
): T {

    val scopeKey = checkNotNull(key ?: kClass?.qualifiedName) {
        "You must specify either class or key to uniquely identify the ViewModel"
    }

    return rememberScope(key = "ViewModelScope$scopeKey") {
        ViewModelScope { creator() }
    }.viewModel
}