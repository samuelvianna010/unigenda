package com.samuelvianna010.unigenda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.samuelvianna010.unigenda.ui.navigation.NavGraph
import com.samuelvianna010.unigenda.ui.theme.UnigendaTheme
import dagger.hilt.android.AndroidEntryPoint

//region Main Activity
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			UnigendaTheme {
				val navController =
					rememberNavController()
				NavGraph(navController = navController)
			}
		}
	}
}
//endregion

//region Preview
@Preview(
	device = Devices.PHONE,
	showBackground = true
)
@Composable
fun simplePreview() {
	UnigendaTheme {
		val navController =
			rememberNavController()
		NavGraph(navController = navController)
	}
}
//endregion