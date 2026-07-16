package com.samuelvianna010.unigenda.ui.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * Formata um Double para exibir o mínimo de casas decimais necessárias.
 * Ex: 5.0 -> "5", 5.5 -> "5.5", 5.55 -> "5.55"
 */
fun Double.formatCompact(): String {
    val df = DecimalFormat("0.##", DecimalFormatSymbols(Locale.US))
    return df.format(this)
}

/**
 * Versão para Double opcional.
 */
fun Double?.formatCompact(fallback: String = "N/A"): String {
    return this?.formatCompact() ?: fallback
}
