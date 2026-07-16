package com.samuelvianna010.unigenda.components

import android.content.res.Configuration
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.samuelvianna010.unigenda.database.Subject
import com.samuelvianna010.unigenda.database.TemplateSubject
import com.samuelvianna010.unigenda.ui.theme.UnigendaTheme

//region Component UI
@Composable
fun SubjectCard(
	subject: Subject,
	onClick: (subjectId: Long) -> Unit = {} // Adiciona um callback de clique
) {
	val colorScheme =
		subject.color.getColorScheme(isSystemInDarkTheme())
	// Interação Source para o Ripple funcionar corretamente sem estado interno
	val interactionSource = remember { MutableInteractionSource() }

	Card(
		modifier = Modifier
			.height(200.dp)
			.fillMaxWidth()
			.clickable(
				interactionSource = interactionSource,
				indication = LocalIndication.current,
				onClick = { onClick(subject.id) }
			),
		elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
		colors = CardDefaults.cardColors(containerColor = colorScheme.primary),
		// O próprio Card já tem ripple se você usar o modifier clickable nele ou no conteúdo
	) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp)
		) {
			//region Subject Info
			Column(
				verticalArrangement = Arrangement.Center,
				modifier = Modifier.fillMaxWidth(0.8f) // Deixa espaço para a seta
			) {
				Text(
					text = subject.name,
					color = colorScheme.onPrimary,
					fontSize = 8.em,
					lineHeight = 0.8.em,
					fontWeight = FontWeight.ExtraBold
				)
				Text(
					text = subject.professor, // Ajuste para o nome correto da sua Entity
					color = colorScheme.onPrimary.copy(alpha = 0.7f),
					fontSize = 5.em,
					fontWeight = FontWeight.SemiBold
				)
			}
			//endregion

			//region Navigation Icon
			// Ícone de "Ir para" no canto direito
			Icon(
				imageVector = Icons.Default.ArrowForward,
				contentDescription = "Ver detalhes",
				tint = colorScheme.onPrimary.copy(alpha = 0.8f),
				modifier = Modifier
					.align(Alignment.CenterEnd)
					.size(32.dp)
			)
			//endregion
		}
	}
}
//endregion

//region Preview
@Preview(
	device = "spec:width=411dp,height=891dp",
	showBackground = true,
	uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun Preview() {
	UnigendaTheme {
		SubjectCard(
			subject = TemplateSubject,
			onClick = { println("Clicou!") }
		)
	}
}
//endregion