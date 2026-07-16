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
//endregion
