package io.github.trueangle.blackbox.multiplatform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import io.github.trueangle.blackbox.core.ViewScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import moe.tlaster.precompose.stateholder.LocalStateHolder
import kotlin.reflect.KClass

abstract class FlowScope : ViewScope() {
    private var _coordinator: Coordinator? = null

    abstract val coordinator: Coordinator

    override fun onDestroy() {
        _coordinator?.onDestroy()
    }
}

@Composable
inline fun <reified T : ViewScope> rememberScope(
    key: String? = null,
    crossinline scopeCreator: () -> T
): T {
    val kClass = getKClassForGenericType<T>()
    val scopeKey = checkNotNull(key ?: kClass.qualifiedName) {
        "It is impossible to use anonymous object name as a key, please specify the key param explicitly"
    }

    val stateHolder = checkNotNull(LocalStateHolder.current) { "LocalStateHolder must not be null" }

    return stateHolder.getOrPut(scopeKey) {
        scopeCreator()
    }
}

@Composable
inline fun <reified T : Coordinator> rememberCoordinator(
    key: String? = null,
    crossinline creator: () -> T
): T {
    val kClass = getKClassForGenericType<T>()
    val scopeKey = checkNotNull(key ?: kClass.qualifiedName) {
        "It is impossible to use anonymous object name as a key, please specify the key param explicitly"
    }

    return rememberScope(key = scopeKey) {
        CoordinatorScope(coordinator = creator())
    }.coordinator as T
}

@Composable
inline fun <reified T : ViewModel> rememberViewModel(
    key: String? = null,
    crossinline creator: () -> T
): T {

    val kClass = getKClassForGenericType<T>()
    val scopeKey = checkNotNull(key ?: kClass.qualifiedName) {
        "It is impossible to use anonymous object name as a key, please specify the key param explicitly"
    }

    return rememberScope(key = "ViewModelScope$scopeKey") {
        ViewModelScope { creator() }
    }.viewModel
}

// workaround method for getting reified type of @Composable fun
// https://github.com/JetBrains/compose-multiplatform/issues/3147
@PublishedApi
internal inline fun <reified T : Any> getKClassForGenericType(): KClass<T> = T::class

@PublishedApi
internal class CoordinatorScope(override val coordinator: Coordinator) : FlowScope()