package com.samuelvianna010.unigenda.ui.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

//region UTC To Local Conversion
/**
 * Converte milissegundos UTC (do DatePicker) para milissegundos na zona horária local.
 * Isso resolve o problema da data selecionada ser salva como o dia anterior.
 */
fun Long.toLocalMillis(): Long {
	val offset = TimeZone.getDefault().getOffset(this)
	return this + offset
}
//endregion

//region Date Formatting
/**
 * Formata milissegundos em uma string de data legível usando o locale padrão.
 */
fun Long.formatDate(pattern: String = "dd/MM/yyyy"): String {
	return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(this))
}

/**
 * Retorna o dia da semana formatado (ex: Segunda-feira).
 */
fun Long.formatToDayOfWeek(): String {
	return SimpleDateFormat("EEEE", Locale.getDefault()).format(Date(this))
}

/**
 * Formata a data de forma descritiva e localizada (ex: "Mon, July 20th 2026" ou "Seg, 20 de Julho de 2026").
 */
fun Long.formatToFullDate(): String {
	val locale = Locale.getDefault()
	val date = Date(this)

	return if (locale.language == "en") {
		// English: "Mon, July 20th 2026"
		val dayOfWeek = SimpleDateFormat("EEE", locale).format(date)
		val month = SimpleDateFormat("MMMM", locale).format(date)
		val year = SimpleDateFormat("yyyy", locale).format(date)

		val dayOfMonth = SimpleDateFormat("d", locale).format(date).toInt()
		val suffix = when {
			dayOfMonth in 11..13 -> "th"
			dayOfMonth % 10 == 1 -> "st"
			dayOfMonth % 10 == 2 -> "nd"
			dayOfMonth % 10 == 3 -> "rd"
			else -> "th"
		}
		"$dayOfWeek, $month $dayOfMonth$suffix $year"
	} else {
		// Default (e.g., Portuguese): "Seg, 20 de Julho de 2026"
		SimpleDateFormat("EEE, d 'de' MMMM 'de' yyyy", locale).format(date)
	}
}

/**
 * Formata o mês e ano (ex: "Julho 2026").
 */
fun Long.formatMonthYear(): String {
	return SimpleDateFormat("MMMM yyyy", Locale.getDefault())
		.format(Date(this))
		.replaceFirstChar { it.uppercase() }
}
//endregion
