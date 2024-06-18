// createFeatureModule.gradle.kts
// !! USAGE: ./gradlew createCleanArchitectureFolders -PmoduleName=notification

tasks.register("createCleanArchitectureFolders") {
    val moduleName = if (project.hasProperty("moduleName")) project.property("moduleName") as String else "defaultModule"
    val packageName = if (project.hasProperty("moduleName")) "az.kodcraft.${project.property("moduleName")}" else "az.kodcraft.default"
    val featureDir = file("${project.projectDir}/feature/$moduleName")
    val baseDir = file("$featureDir/src/main/java/${packageName.replace('.', '/')}")

    doLast {
        // Ensure the base directory is created
        baseDir.mkdirs()

        // List of folders to create
        val folders = listOf(
            "data/di",
            "data/dto",
            "data/repository",
            "data/service",
            "navigation",
            "domain/model",
            "domain/repository",
            "domain/usecase",
            "presentation/contract",
            "presentation/composable",
            "presentation/di"
        )

        // Create each folder
        folders.forEach { folder ->
            file("$baseDir/$folder").mkdirs()
        }

        // Example template for a ViewModel
        val viewModelTemplate = """
            package $packageName.presentation

            import androidx.lifecycle.SavedStateHandle
            import az.kodcraft.core.presentation.bases.BaseViewModel
            import dagger.hilt.android.lifecycle.HiltViewModel
            import javax.inject.Inject
            import kotlinx.coroutines.flow.Flow
            import kotlinx.coroutines.flow.flow
            import $packageName.presentation.contract.${moduleName.capitalize()}UiState
            import $packageName.presentation.contract.${moduleName.capitalize()}Event
            import $packageName.presentation.contract.${moduleName.capitalize()}Intent

            @HiltViewModel
            class ${moduleName.capitalize()}ViewModel @Inject constructor(
                savedStateHandle: SavedStateHandle,
                initialState: ${moduleName.capitalize()}UiState
                // private val someUseCase: SomeUseCase, // Replace with actual use case(s)
            ) : BaseViewModel<
                ${moduleName.capitalize()}UiState, 
                ${moduleName.capitalize()}UiState.PartialState, 
                ${moduleName.capitalize()}Event, 
                ${moduleName.capitalize()}Intent>(
                savedStateHandle,
                initialState
            ) {

                override fun mapIntents(intent: ${moduleName.capitalize()}Intent): Flow<${moduleName.capitalize()}UiState.PartialState> =
                    when (intent) {
                        is ${moduleName.capitalize()}Intent.SomeIntent -> handleSomeIntent()
                        // Add more intents here
                    }

                private fun handleSomeIntent(): Flow<${moduleName.capitalize()}UiState.PartialState> =
                    flow {
                        // someUseCase.execute(id).doOnSuccess {
                        //     emit(${moduleName.capitalize()}UiState.PartialState.SomeResult(it))
                        // }.doOnLoading {
                        //     emit(${moduleName.capitalize()}UiState.PartialState.Loading)
                        // }.doOnFailure {
                        //     emit(${moduleName.capitalize()}UiState.PartialState.Error(it))
                        // }.collect()
                    }

                override fun reduceUiState(
                    previousState: ${moduleName.capitalize()}UiState,
                    partialState: ${moduleName.capitalize()}UiState.PartialState
                ) = when (partialState) {
                    ${moduleName.capitalize()}UiState.PartialState.Loading -> previousState.copy(
                        isLoading = true,
                        isError = false,
                        errorMessage = ""
                    )
                    is ${moduleName.capitalize()}UiState.PartialState.SomeResult -> previousState.copy(
                        isLoading = false,
                        isError = false,
                        errorMessage = ""
                    )
                    is ${moduleName.capitalize()}UiState.PartialState.Error -> previousState.copy(
                        isLoading = false,
                        isError = true,
                        errorMessage = partialState.message
                    )
                }
            }
        """.trimIndent()

        // Create the ViewModel file
        file("$baseDir/presentation/${moduleName.capitalize()}ViewModel.kt").apply {
            parentFile.mkdirs()
            writeText(viewModelTemplate)
        }

        // Create the DI module file
        val diModuleTemplate = """
            package $packageName.presentation.di

            import dagger.Module
            import dagger.Provides
            import dagger.hilt.InstallIn
            import dagger.hilt.android.components.ViewModelComponent
            import $packageName.presentation.contract.${moduleName.capitalize()}UiState

            @Module
            @InstallIn(ViewModelComponent::class)
            object ${moduleName.capitalize()}PresentationModule {
                @Provides
                fun provide${moduleName.capitalize()}UiState(): ${moduleName.capitalize()}UiState =
                    ${moduleName.capitalize()}UiState()
            }
        """.trimIndent()

        // Create the DI module file
        file("$baseDir/presentation/di/${moduleName.capitalize()}PresentationModule.kt").apply {
            parentFile.mkdirs()
            writeText(diModuleTemplate)
        }

        // Create the Route class file
        val routeTemplate = """
            package $packageName.presentation

            import android.widget.Toast
            import androidx.compose.foundation.background
            import androidx.compose.foundation.layout.*
            import androidx.compose.foundation.lazy.LazyColumn
            import androidx.compose.foundation.lazy.items
            import androidx.compose.material3.MaterialTheme
            import androidx.compose.material3.Text
            import androidx.compose.runtime.Composable
            import androidx.compose.runtime.LaunchedEffect
            import androidx.compose.runtime.collectAsState
            import androidx.compose.runtime.getValue
            import androidx.compose.ui.Alignment
            import androidx.compose.ui.Modifier
            import androidx.compose.ui.platform.LocalContext
            import androidx.compose.ui.unit.dp
            import androidx.hilt.navigation.compose.hiltViewModel
            import az.kodcraft.core.utils.collectWithLifecycle
            import $packageName.presentation.contract.${moduleName.capitalize()}Event
            import $packageName.presentation.contract.${moduleName.capitalize()}Intent
            import $packageName.presentation.contract.${moduleName.capitalize()}UiState

            @Composable
            fun ${moduleName.capitalize()}Route(
                viewModel: ${moduleName.capitalize()}ViewModel = hiltViewModel(),
                navigateBack: () -> Unit,
                navigateToUserProfile: (id: String) -> Unit,
            ) {
                val uiState by viewModel.uiState.collectAsState()
                val context = LocalContext.current
                LaunchedEffect(uiState.errorMessage) {
                    if (uiState.errorMessage.isNotBlank())
                        Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
                }
//                viewModel.event.collectWithLifecycle {
//                    when (it) {
//                        ${moduleName.capitalize()}Event.NavigateToDashboard -> navigateBack()
//                    }
//                }
                ${moduleName.capitalize()}Screen(
                    uiState = uiState,
                    onIntent = viewModel::acceptIntent,
                )
            }

            @Composable
            fun ${moduleName.capitalize()}Screen(
                uiState: ${moduleName.capitalize()}UiState,
                onIntent: (${moduleName.capitalize()}Intent) -> Unit = {}
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                ) {
                    // Your screen content here
                }
            }
        """.trimIndent()

        // Create the Route class file
        file("$baseDir/presentation/${moduleName.capitalize()}Route.kt").apply {
            parentFile.mkdirs()
            writeText(routeTemplate)
        }


        // Create the build.gradle.kts file
        val buildGradleContent = """
            plugins {
                alias(libs.plugins.androidLibrary)
                alias(libs.plugins.jetbrainsKotlinAndroid)
                kotlin("kapt")
                id("com.google.dagger.hilt.android")
                id("kotlin-parcelize")
            }

            android {
                namespace = "$packageName"
            }

            dependencies {
                implementation(project(":core"))
                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.appcompat)
                implementation(libs.material)
                testImplementation(libs.junit)
                implementation(libs.androidx.material3)
                implementation(platform(libs.androidx.compose.bom))
                implementation(libs.androidx.ui.tooling)
                implementation(libs.androidx.ui.tooling.preview)

                implementation(libs.androidx.hilt.navigation.compose)
                implementation(libs.hilt.android)
                kapt(libs.hilt.compiler)

                implementation(libs.androidx.navigation.compose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.androidx.lifecycle.viewModelCompose)

                // Import the Firebase
                implementation(platform(libs.firebase.bom))
                implementation(libs.firebase.analytics)
                implementation(libs.firebase.firestore)

                androidTestImplementation(libs.androidx.junit)
                androidTestImplementation(libs.androidx.espresso.core)
            }

            kapt {
                correctErrorTypes = true
            }
        """.trimIndent()

        file("$featureDir/build.gradle.kts").apply {
            parentFile.mkdirs()
            writeText(buildGradleContent)
        }

        // Create the AndroidManifest.xml file
        val androidManifestContent = """
            <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                package="$packageName">
                <application />
            </manifest>
        """.trimIndent()

        file("$featureDir/src/main/AndroidManifest.xml").apply {
            parentFile.mkdirs()
            writeText(androidManifestContent)
        }

        // Create the proguard-rules.pro file
        val proguardRulesContent = """
            # Add your ProGuard rules here.
            # By default, the flags in this file are appended to flags specified
            # in /usr/local/Cellar/android-sdk/24.3.3/tools/proguard/proguard-android.txt
            # You can edit the include path and order by changing the proguardFiles
            # directive in build.gradle.
        """.trimIndent()

        file("$featureDir/proguard-rules.pro").apply {
            parentFile.mkdirs()
            writeText(proguardRulesContent)
        }

        // Create the .gitignore file
        val gitignoreContent = """
            /build
        """.trimIndent()

        file("$featureDir/.gitignore").apply {
            parentFile.mkdirs()
            writeText(gitignoreContent)
        }

        // Append to settings.gradle.kts file
        val settingsGradleFile = file("${project.projectDir}/settings.gradle.kts")
        if (settingsGradleFile.exists()) {
            settingsGradleFile.appendText("\ninclude(\":feature:$moduleName\")\n")
        }
    }
}

// Subtask to populate contract folder
tasks.register("populateContractFolder") {
    val moduleName = if (project.hasProperty("moduleName")) project.property("moduleName") as String else "defaultModule"
    val packageName = if (project.hasProperty("moduleName")) "az.kodcraft.${project.property("moduleName")}" else "az.kodcraft.default"
    val baseDir = file("${project.projectDir}/feature/$moduleName/src/main/java/${packageName.replace('.', '/')}")

    doLast {
        // Create UiState class
        val uiStateTemplate = """
            package $packageName.presentation.contract

            import android.os.Parcelable
            import kotlinx.parcelize.Parcelize

            @Parcelize
            data class ${moduleName.capitalize()}UiState(
                val exampleField: String = "",
                val isLoading: Boolean = true,
                val isError: Boolean = false,
                val errorMessage: String = ""
            ) : Parcelable {
                sealed class PartialState {
                    data class Error(val message: String) : PartialState()
                    data object Loading : PartialState()
                    data class SomeResult(val value: String) : PartialState()
                }
            }
        """.trimIndent()

        file("$baseDir/presentation/contract/${moduleName.capitalize()}UiState.kt").apply {
            parentFile.mkdirs()
            writeText(uiStateTemplate)
        }

        // Create Event class
        val eventTemplate = """
            package $packageName.presentation.contract

            sealed class ${moduleName.capitalize()}Event {
                object ExampleEvent : ${moduleName.capitalize()}Event()
            }
        """.trimIndent()

        file("$baseDir/presentation/contract/${moduleName.capitalize()}Event.kt").apply {
            parentFile.mkdirs()
            writeText(eventTemplate)
        }

        // Create Intent class
        val intentTemplate = """
            package $packageName.presentation.contract

            sealed class ${moduleName.capitalize()}Intent {
                object SomeIntent : ${moduleName.capitalize()}Intent()
            }
        """.trimIndent()

        file("$baseDir/presentation/contract/${moduleName.capitalize()}Intent.kt").apply {
            parentFile.mkdirs()
            writeText(intentTemplate)
        }
    }
}

// Ensure populateContractFolder runs after createCleanArchitectureFolders
tasks.named("createCleanArchitectureFolders").configure {
    finalizedBy("populateContractFolder", "syncProject")
}

// Task to sync the project
tasks.register("syncProject") {
    doLast {
        // Trigger Gradle sync
        println("Syncing project...")
        gradle.startParameter.isRefreshDependencies = true
    }
}
