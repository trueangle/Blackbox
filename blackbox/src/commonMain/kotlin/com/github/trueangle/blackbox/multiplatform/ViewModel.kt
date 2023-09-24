package com.github.trueangle.blackbox.multiplatform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.github.trueangle.blackbox.core.ViewScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

// Predefined scopes
@Stable
abstract class ViewModel {
    protected val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    open fun onDestroy() {
        coroutineScope.cancel()
    }
}

class ViewModelScope<T : ViewModel>(val onCreate: () -> T) : ViewScope() {

    private val _viewModel = lazy { onCreate() }
    val viewModel by _viewModel

    override fun onDestroy() {
        if (_viewModel.isInitialized()) {
            viewModel.onDestroy()
        }
    }
}

