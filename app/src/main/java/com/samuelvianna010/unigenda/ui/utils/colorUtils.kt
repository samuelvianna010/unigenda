package com.samuelvianna010.unigenda.core.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

/**
 * Gera um ColorScheme claro completo e balanceado baseado em uma cor semente.
 * Define explicitamente todos os containers de superfície para evitar o tom rosado padrão.
 */
fun generateLightColorScheme(seedColor: Color): ColorScheme {
    val seedArgb = seedColor.toArgb()
    
    // Cores de destaque derivadas (tons complementares e análogos sutis)
    val secondary = Color(ColorUtils.blendARGB(seedArgb, Color(0xFF5D5E71).toArgb(), 0.5f))
    val tertiary = Color(ColorUtils.blendARGB(seedArgb, Color(0xFF7E5260).toArgb(), 0.5f))

    return lightColorScheme(
        primary = seedColor,
        onPrimary = Color.White,
        primaryContainer = Color(ColorUtils.blendARGB(seedArgb, Color.White.toArgb(), 0.85f)),
        onPrimaryContainer = Color(ColorUtils.blendARGB(seedArgb, Color.Black.toArgb(), 0.7f)),
        
        secondary = secondary,
        onSecondary = Color.White,
        secondaryContainer = Color(ColorUtils.blendARGB(secondary.toArgb(), Color.White.toArgb(), 0.85f)),
        onSecondaryContainer = Color(ColorUtils.blendARGB(secondary.toArgb(), Color.Black.toArgb(), 0.7f)),

        tertiary = tertiary,
        onTertiary = Color.White,
        tertiaryContainer = Color(ColorUtils.blendARGB(tertiary.toArgb(), Color.White.toArgb(), 0.85f)),
        onTertiaryContainer = Color(ColorUtils.blendARGB(tertiary.toArgb(), Color.Black.toArgb(), 0.7f)),
        
        background = Color(ColorUtils.blendARGB(seedArgb, Color.White.toArgb(), 0.98f)),
        onBackground = Color(0xFF1C1B1F),
        
        surface = Color(ColorUtils.blendARGB(seedArgb, Color.White.toArgb(), 0.98f)),
        onSurface = Color(0xFF1C1B1F),
        surfaceVariant = Color(ColorUtils.blendARGB(seedArgb, Color.White.toArgb(), 0.90f)),
        onSurfaceVariant = Color(0xFF49454F),
        
        surfaceContainerLowest = Color.White,
        surfaceContainerLow = Color(ColorUtils.blendARGB(seedArgb, Color.White.toArgb(), 0.96f)),
        surfaceContainer = Color(ColorUtils.blendARGB(seedArgb, Color.White.toArgb(), 0.92f)),
        surfaceContainerHigh = Color(ColorUtils.blendARGB(seedArgb, Color.White.toArgb(), 0.88f)),
        surfaceContainerHighest = Color(ColorUtils.blendARGB(seedArgb, Color.White.toArgb(), 0.84f)),
        
        outline = Color(0xFF79747E),
        error = Color(0xFFB3261E),
        onError = Color.White
    )
}

/**
 * Gera um ColorScheme escuro completo baseado em uma cor semente.
 */
fun generateDarkColorScheme(seedColor: Color): ColorScheme {
    val seedArgb = seedColor.toArgb()
    
    val secondary = Color(ColorUtils.blendARGB(seedArgb, Color(0xFFC9C5D0).toArgb(), 0.6f))
    val tertiary = Color(ColorUtils.blendARGB(seedArgb, Color(0xFFEFB8C8).toArgb(), 0.6f))

    return darkColorScheme(
        primary = seedColor,
        onPrimary = Color.Black,
        primaryContainer = Color(ColorUtils.blendARGB(seedArgb, Color.Black.toArgb(), 0.4f)),
        onPrimaryContainer = Color(ColorUtils.blendARGB(seedArgb, Color.White.toArgb(), 0.7f)),

        secondary = secondary,
        onSecondary = Color.Black,
        secondaryContainer = Color(ColorUtils.blendARGB(secondary.toArgb(), Color.Black.toArgb(), 0.3f)),
        onSecondaryContainer = Color(ColorUtils.blendARGB(secondary.toArgb(), Color.White.toArgb(), 0.7f)),

        tertiary = tertiary,
        onTertiary = Color.Black,
        tertiaryContainer = Color(ColorUtils.blendARGB(tertiary.toArgb(), Color.Black.toArgb(), 0.3f)),
        onTertiaryContainer = Color(ColorUtils.blendARGB(tertiary.toArgb(), Color.White.toArgb(), 0.7f)),
        
        background = Color(ColorUtils.blendARGB(seedArgb, Color.Black.toArgb(), 0.96f)),
        onBackground = Color(0xFFE6E1E5),
        
        surface = Color(ColorUtils.blendARGB(seedArgb, Color.Black.toArgb(), 0.96f)),
        onSurface = Color(0xFFE6E1E5),
        
        surfaceContainerLowest = Color(0xFF0F0E11),
        surfaceContainerLow = Color(ColorUtils.blendARGB(seedArgb, Color.Black.toArgb(), 0.85f)),
        surfaceContainer = Color(ColorUtils.blendARGB(seedArgb, Color.Black.toArgb(), 0.80f)),
        surfaceContainerHigh = Color(ColorUtils.blendARGB(seedArgb, Color.Black.toArgb(), 0.75f)),
        surfaceContainerHighest = Color(ColorUtils.blendARGB(seedArgb, Color.Black.toArgb(), 0.70f)),
        
        outline = Color(0xFF938F99),
        error = Color(0xFFF2B8B5),
        onError = Color(0xFF601410)
    )
}

/**
 * Enumeração de cores disponíveis para as matérias (Subjects).
 * Cada cor pode fornecer seu próprio ColorScheme para temas claro e escuro.
 */
enum class SubjectColor(
	val label: String,
	val color: Color
) {
	PURPLE("Roxo", Color(0xFF6750A4)),
	RED("Vermelho", Color(0xFFB3261E)),
	BLUE("Azul", Color(0xFF2196F3)),
	GREEN("Verde", Color(0xFF4CAF50)),
	ORANGE("Laranja", Color(0xFFFF9800)),
	PINK("Rosa", Color(0xFFE91E63)),
	TEAL("Teal", Color(0xFF009688)),
	BROWN("Marrom", Color(0xFF795548));

	/**
	 * Retorna o ColorScheme correspondente à cor da matéria e ao tema atual.
	 */
	fun getColorScheme(isDark: Boolean): ColorScheme {
		return if (isDark) generateDarkColorScheme(
			color
		) else generateLightColorScheme(color)
	}

	companion object {
		fun fromColorValue(value: Int): SubjectColor? {
			return entries.find { it.color.toArgb() == value }
		}
	}
}

// Mantendo para compatibilidade se necessário, agora derivado do enum
val SubjectColors =
	SubjectColor.entries.map { it.color }
