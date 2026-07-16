plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("com.google.dagger.hilt.android")
	id("com.google.devtools.ksp")
	id("org.jetbrains.kotlin.plugin.serialization")
}

android {
	namespace =
		"com.samuelvianna010.unigenda"
	compileSdk = 34

	defaultConfig {
		applicationId =
			"com.samuelvianna010.unigenda"
		minSdk =
			27
		targetSdk =
			34
		versionCode =
			1
		versionName =
			"1.0"

		testInstrumentationRunner =
			"androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
			isMinifyEnabled =
				false
			proguardFiles(
				getDefaultProguardFile(
					"proguard-android-optimize.txt"
				),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility =
			JavaVersion.VERSION_11
		targetCompatibility =
			JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = "11"
	}
	buildFeatures {
		compose =
			true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.10" // Compatible with Kotlin 1.9.22
	}
}

kotlin {
	jvmToolchain(11)
}

dependencies {
	// Navegação

	implementation("androidx.navigation:navigation-compose:2.8.5")

	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

	// ViewModel & Lifecycle
	implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
	implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

	// Hilt (DI)
	implementation("com.google.dagger:hilt-android:2.51.1")
	implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
	ksp("com.google.dagger:hilt-compiler:2.51.1")

	// Room (SQL)
	implementation("androidx.room:room-runtime:2.6.1")
	implementation("androidx.room:room-ktx:2.6.1")
	ksp("androidx.room:room-compiler:2.6.1")
	implementation(
		platform(
			libs.androidx.compose.bom
		)
	)
	implementation(
		libs.androidx.activity.compose
	)
	implementation(
		libs.androidx.compose.material3
	)
	implementation(
		libs.androidx.compose.ui
	)
	implementation(
		libs.androidx.compose.ui.graphics
	)
	implementation(
		libs.androidx.compose.ui.tooling.preview
	)
	implementation(
		libs.androidx.core.ktx
	)
	implementation(
		libs.androidx.lifecycle.runtime.ktx
	)
	testImplementation(
		libs.junit
	)
	androidTestImplementation(
		platform(
			libs.androidx.compose.bom
		)
	)
	androidTestImplementation(
		libs.androidx.compose.ui.test.junit4
	)
	androidTestImplementation(
		libs.androidx.espresso.core
	)
	androidTestImplementation(
		libs.androidx.junit
	)
	debugImplementation(
		libs.androidx.compose.ui.test.manifest
	)
	debugImplementation(
		libs.androidx.compose.ui.tooling
	)
}
