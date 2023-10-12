package io.github.trueangle.blackbox.android

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.trueangle.blackbox.core.ViewScope

@PublishedApi
internal class ScopeHolder<VS : ViewScope>(
    val scope: VS
) : ViewModel() {

    init {
        Log.d("ScopeHolder", "Init")
    }

    override fun onCleared() {
        Log.d("ScopeHolder", "On cleared")
        scope.close()
    }

    class Factory<VS : ViewScope>(
        private val creator: (SavedStateHandle) -> VS
    ) : AbstractSavedStateViewModelFactory() {
        override fun <T : ViewModel> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T {
            @Suppress("UNCHECKED_CAST")
            return ScopeHolder(creator(handle)) as T
        }
    }
}

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
inline fun <reified T : ViewScope> rememberScope(
    crossinline scopeCreator: () -> T
): T {
    val canonicalName = T::class.java.canonicalName
        ?: throw IllegalArgumentException("Local and anonymous classes can not be ViewScopes")

    // saved instance hanndle
    /*    val holder = viewModel(key = "FlowScopeHolder:$canonicalName", factory = ScopeHolder.Factory {
            scopeCreator()
        }) as ScopeHolder<T>
        */
    val holder2 = viewModel(key = "FlowScopeHolder:$canonicalName") {
        ScopeHolder(scopeCreator())
    }

    return holder2.scope
}