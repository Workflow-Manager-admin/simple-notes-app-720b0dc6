package com.example.notesfrontend.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val primary = Color(0xFF2196F3)
private val secondary = Color(0xFF1976D2)
private val accent = Color(0xFFFFEB3B)
private val background = Color(0xFFFFFFFF)
private val surface = Color(0xFFF7F9FB)
private val onPrimary = Color.White
private val onSecondary = Color.White
private val onSurface = Color(0xFF202020)
private val onBackground = Color(0xFF222222)
private val error = Color(0xFFB00020)

/**
 * Light color palette based on minimalistic, modern look.
 */
private val LightColorScheme = lightColorScheme(
    primary = primary,
    onPrimary = onPrimary,
    secondary = secondary,
    onSecondary = onSecondary,
    background = background,
    surface = surface,
    onBackground = onBackground,
    onSurface = onSurface,
    error = error,
    onError = Color.White
)

@Composable
// PUBLIC_INTERFACE
fun NotesFrontendTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        content = content
    )
}
