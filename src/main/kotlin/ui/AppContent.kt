package ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ui.navigation.Location
import ui.pages.ArchivePage
import ui.pages.NotesPage
import ui.state.AppState

@Composable
internal fun AppContent(
    modifier: Modifier = Modifier,
    compact: Boolean,
    scope: CoroutineScope,
    snackbarState: SnackbarHostState,
    viewModel: AppState
) {
    val currentLocation by remember { viewModel.currentLocation }

    when (currentLocation) {
        Location.MAIN -> NotesPage(
            compact = compact,
            modifier = modifier.fillMaxSize(),
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
            state = viewModel.notesState
        )
        Location.ARCHIVE -> ArchivePage(
            compact = compact,
            modifier = modifier.fillMaxSize(),
            state = viewModel.archivedNotesState,
        )
    }
}