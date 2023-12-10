package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import data.repository.InMemoryNoteRepository
import data.repository.NoteRepository
import ui.navigation.Location
import ui.state.AppState
import ui.widgets.LoadingScreen

internal data class AppNavigationDestination(
    val icon: ImageVector,
    val label: String,
    val location: Location,
)

@Composable
@Preview
fun App(noteRepository: NoteRepository = InMemoryNoteRepository()) {
    val appState = remember { AppState(noteRepository) }

    var currentLocation by remember { appState.currentLocation }
    val destinations = listOf(
        AppNavigationDestination(Icons.Outlined.Lightbulb, "Заметки", Location.MAIN),
        AppNavigationDestination(Icons.Outlined.Archive, "Архив", Location.ARCHIVE),
    )
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        appState.initialize()
    }

    AppTheme {
        BoxWithConstraints {
            val mediumUi = maxWidth < 1240.dp
            val compactUi = maxWidth < 600.dp

            Scaffold(
                bottomBar = {
                    if (compactUi) {
                        NavigationBar {
                            destinations.forEach { destination ->
                                NavigationBarItem(
                                    selected = currentLocation == destination.location,
                                    onClick = { currentLocation = destination.location },
                                    icon = { Icon(destination.icon, destination.label) },
                                    label = { Text(destination.label) },
                                )
                            }
                        }
                    }
                },
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                }
            ) { innerPadding ->
                AppContentNavigator(
                    compactUi = compactUi,
                    currentLocation = currentLocation,
                    destinations = destinations,
                    mediumUi = mediumUi,
                    modifier = Modifier.padding(innerPadding),
                    navigate = { currentLocation = it }
                ) { innerModifier ->
                    if (appState.initialized.value) {
                        AppContent(
                            modifier = innerModifier,
                            compact = compactUi,
                            scope = scope,
                            snackbarState = snackbarHostState,
                            viewModel = appState
                        )
                    } else {
                        LoadingScreen(modifier = innerModifier.fillMaxSize())
                    }
                }
            }
        }
    }
}