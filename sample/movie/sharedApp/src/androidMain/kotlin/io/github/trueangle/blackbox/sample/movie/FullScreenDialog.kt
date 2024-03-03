package io.github.trueangle.blackbox.sample.movie

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider

@Composable
fun Dialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = properties.dismissOnBackPress,
            dismissOnClickOutside = properties.dismissOnClickOutside,
            securePolicy = properties.securePolicy,
            usePlatformDefaultWidth = properties.usePlatformDefaultWidth, // must be true as a part of work around
            decorFitsSystemWindows = properties.decorFitsSystemWindows
        ),
        content = {
            if(!properties.decorFitsSystemWindows) {
                val activityWindow = getActivityWindow()
                val dialogWindow = getDialogWindow()
                val parentView = LocalView.current.parent as View

                SideEffect {
                    if (activityWindow != null && dialogWindow != null) {
                        val attributes = WindowManager.LayoutParams()
                        attributes.copyFrom(activityWindow.attributes)
                        attributes.type = dialogWindow.attributes.type

                        dialogWindow.attributes = attributes

                        parentView.layoutParams = FrameLayout.LayoutParams(
                            activityWindow.decorView.width,
                            activityWindow.decorView.height
                        )
                    }
                }
            }

           /* val systemUiController = rememberSystemUiController(getActivityWindow())
            val dialogSystemUiController = rememberSystemUiController(getDialogWindow())

            DisposableEffect(Unit) {
                systemUiController.setSystemBarsColor(color = Color.Transparent)
                dialogSystemUiController.setSystemBarsColor(color = Color.Transparent)

                onDispose {
                    systemUiController.setSystemBarsColor(color = previousSystemBarsColor)
                    dialogSystemUiController.setSystemBarsColor(color = previousSystemBarsColor)
                }
            }

            // If you need Immersive mode
            val isImmersive = true
            DisposableEffect(isImmersive) {
                systemUiController.isSystemBarsVisible = !isImmersive
                dialogSystemUiController.isSystemBarsVisible = !isImmersive

                onDispose {
                    systemUiController.isSystemBarsVisible = true
                    dialogSystemUiController.isSystemBarsVisible = true
                }
            }*/

            Surface(modifier = Modifier.fillMaxSize(), color = Color.Transparent) {
                content()
            }
        }
    )
}

@Composable
private fun getDialogWindow(): Window? = (LocalView.current.parent as? DialogWindowProvider)?.window

@Composable
private fun getActivityWindow(): Window? = LocalView.current.context.getActivityWindow()

private tailrec fun Context.getActivityWindow(): Window? =
    when (this) {
        is Activity -> window
        is ContextWrapper -> baseContext.getActivityWindow()
        else -> null
    }