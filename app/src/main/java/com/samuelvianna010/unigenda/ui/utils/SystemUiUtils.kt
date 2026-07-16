package com.samuelvianna010.unigenda.core.ui

import android.app.Activity
import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

//region Status Bar Color
/**
 * Função interna (não Composable) para definir a cor da barra de status.
 */
fun setStatusBarColorInternal(window: Window, color: Color) {
    window.statusBarColor = color.toArgb()
    val isLightColor = color.red * 0.299 + color.green * 0.587 + color.blue * 0.114 > 0.5
    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = isLightColor
}

/**
 * Composable wrapper para ser usado dentro da árvore de UI.
 */
@Composable
fun SetStatusBarColor(color: Color) {
    val view = LocalView.current
    SideEffect {
        val window = (view.context as? Activity)?.window
        if (window != null) {
            setStatusBarColorInternal(window, color)
        }
    }
}
//endregion
