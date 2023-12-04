package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ui.data.state.AppState
import ui.navigation.Location
import ui.pages.ArchivePage
import ui.pages.NotesPage
import ui.viewmodel.AppViewModel

private data class AppNavigationDestination(
    val icon: ImageVector,
    val label: String,
    val location: Location,
)

@Composable
@Preview
fun App(viewModel: AppViewModel = AppViewModel()) {
    val destinations = listOf(
        AppNavigationDestination(Icons.Outlined.Lightbulb, "Заметки", Location.MAIN),
        AppNavigationDestination(Icons.Outlined.Archive, "Архив", Location.ARCHIVE),
    )
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.state.collectAsState()

    AppTheme {
        BoxWithConstraints {
            val compactUi = maxWidth < 500.dp

            Scaffold(
                bottomBar = {
                    if (compactUi) {
                        NavigationBar {
                            destinations.forEach { destination ->
                                NavigationBarItem(
                                    selected = state.currentLocation == destination.location,
                                    onClick = { viewModel.navigate(destination.location) },
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
                if (compactUi) {
                    AppContent(
                        compact = compactUi,
                        innerPadding = innerPadding,
                        scope = scope,
                        snackbarState = snackbarHostState,
                        state = state,
                        viewModel = viewModel
                    )
                } else {
                    PermanentNavigationDrawer(
                        drawerContent = {
                            PermanentDrawerSheet(Modifier.width(IntrinsicSize.Min)) {
                                Spacer(Modifier.height(12.dp))
                                destinations.forEach { destination ->
                                    NavigationDrawerItem(
                                        label = { Text(destination.label) },
                                        selected = state.currentLocation == destination.location,
                                        onClick = { viewModel.navigate(destination.location) },
                                        icon = { Icon(destination.icon, destination.label) },
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxHeight().padding(innerPadding),
                    ) {
                        AppContent(
                            compact = compactUi,
                            scope = scope,
                            snackbarState = snackbarHostState,
                            state = state,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AppContent(
    compact: Boolean,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    scope: CoroutineScope,
    snackbarState: SnackbarHostState,
    state: AppState,
    viewModel: AppViewModel
) {
    when (state.currentLocation) {
        Location.MAIN -> NotesPage(
            state = state.notesPageState,
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            compact = compact,
            onArchiveNote = { archivedNote ->
                viewModel.archiveNote(archivedNote)
                scope.launch {
                    val result = snackbarState
                        .showSnackbar(
                            message = "Заметка с заголовком \"${archivedNote.header}\" помещена в архив",
                            actionLabel = "Вернуть",
                            duration = SnackbarDuration.Short
                        )
                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            viewModel.unarchiveNote(archivedNote)
                        }
                        SnackbarResult.Dismissed -> {}
                    }

                }
            },
            onBack = viewModel::returnToList,
            onEdit = viewModel::editNoteDraft,
            onResetChanges = viewModel::resetChanges,
            onSaveNote = viewModel::saveNoteDraft,
            onStartEditing = viewModel::startEditing,
            onViewNewNoteDraft = viewModel::viewNewNoteDraft,
            onViewNote = viewModel::viewNote
        )

        Location.ARCHIVE -> ArchivePage(
            state = state.archivePageState,
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            compact = compact,
            onBack = viewModel::returnToList,
            onDeleteNote = viewModel::deleteNote,
            onUnarchiveNote = viewModel::unarchiveNote,
            onViewNote = viewModel::viewNote
        )
    }

}