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
import entity.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import navigation.ActivePane
import navigation.Location
import ui.data.state.ArchivePageState
import ui.data.state.NotesPageState
import ui.pages.ArchivePage
import ui.pages.NotesPage
import kotlin.random.Random

private data class AppNavigationDestination(
    val icon: ImageVector,
    val label: String,
    val location: Location,
)

@Composable
@Preview
fun App() {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val destinations = listOf(
        AppNavigationDestination(Icons.Outlined.Lightbulb, "Заметки", Location.MAIN),
        AppNavigationDestination(Icons.Outlined.Archive, "Архив", Location.ARCHIVE),
    )
    var location by remember { mutableStateOf(Location.MAIN) }

    val archiveState = remember {
        mutableStateOf(
            ArchivePageState(
                activePane = ActivePane.LIST,
                notes = emptyList(),
                viewedNote = null,
            )
        )
    }
    val notesState = remember {
        mutableStateOf(
            NotesPageState(
                activePane = ActivePane.LIST,
                draft = null,
                notes = emptyList(),
                viewedNote = null,
            )
        )
    }

    AppTheme {
        BoxWithConstraints {
            val compactUi = maxWidth < 500.dp

            Scaffold(
                bottomBar = {
                    if (compactUi && (
                            location == Location.MAIN && notesState.value.activePane == ActivePane.LIST
                                    || location == Location.ARCHIVE && archiveState.value.activePane == ActivePane.LIST)) {
                        NavigationBar {
                            destinations.forEach { destination ->
                                NavigationBarItem(
                                    selected = location == destination.location,
                                    onClick = { location = destination.location },
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
                        archiveState = archiveState,
                        compact = compactUi,
                        location = location,
                        notesState = notesState,
                        scope = scope,
                        snackbarState = snackbarHostState,
                    )
                } else {
                    PermanentNavigationDrawer(
                        drawerContent = {
                            PermanentDrawerSheet(Modifier.width(IntrinsicSize.Min)) {
                                Spacer(Modifier.height(12.dp))
                                destinations.forEach { destination ->
                                    NavigationDrawerItem(
                                        label = { Text(destination.label) },
                                        selected = location == destination.location,
                                        onClick = { location = destination.location },
                                        icon = { Icon(destination.icon, destination.label) },
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxHeight().padding(innerPadding),
                    ) {
                        AppContent(
                            archiveState = archiveState,
                            compact = compactUi,
                            location = location,
                            notesState = notesState,
                            scope = scope,
                            snackbarState = snackbarHostState,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AppContent(
    archiveState: MutableState<ArchivePageState>,
    compact: Boolean,
    location: Location,
    notesState: MutableState<NotesPageState>,
    scope: CoroutineScope,
    snackbarState: SnackbarHostState,
) {
    when (location) {
        Location.MAIN -> NotesPage(
            state = notesState.value,
            modifier = Modifier.fillMaxSize(),
            compact = compact,
            onArchiveNote = { archivedNote ->
                archivedNote.archivedAt = Clock.System.now()

                notesState.value = notesState.value.copy(
                    notes = notesState.value.notes - archivedNote,
                    viewedNote =
                        if (notesState.value.viewedNote == archivedNote) null
                        else notesState.value.viewedNote
                )
                archiveState.value = archiveState.value.copy(
                    notes = archiveState.value.notes + archivedNote
                )

                scope.launch {
                    val result = snackbarState
                        .showSnackbar(
                            message = "Заметка с заголовком \"${archivedNote.header}\" помещена в архив",
                            actionLabel = "Вернуть",
                            duration = SnackbarDuration.Short
                        )
                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            archivedNote.archivedAt = null

                            archiveState.value = archiveState.value.copy(
                                notes = archiveState.value.notes - archivedNote,
                                viewedNote =
                                    if (archiveState.value.viewedNote == archivedNote) null
                                    else archiveState.value.viewedNote
                            )
                            notesState.value = notesState.value.copy(
                                notes = notesState.value.notes + archivedNote
                            )
                        }
                        SnackbarResult.Dismissed -> {}
                    }

                }
            },
            onSaveNote = { savedDraft ->
                val editedNote = if (savedDraft.id == null) null else notesState.value.notes.firstOrNull {
                    it.id == savedDraft.id
                }

                if (editedNote == null) {
                    val createdNote = Note(
                        id = Random.nextLong(),
                        createdAt = Clock.System.now(),
                        archivedAt = null,
                        header = savedDraft.header,
                        content = savedDraft.content,
                    )
                    notesState.value = notesState.value.copy(
                        activePane = ActivePane.LIST,
                        draft = null,
                        notes = notesState.value.notes + createdNote,
                        viewedNote = createdNote,
                    )
                } else {
                    val newEditedNoteVersion = editedNote.copy(
                        lastUpdatedAt = Clock.System.now(),
                        header = savedDraft.header,
                        content = savedDraft.content,
                    )
                    notesState.value = notesState.value.copy(
                        activePane = ActivePane.LIST,
                        draft = null,
                        notes = notesState.value.notes - editedNote + newEditedNoteVersion,
                        viewedNote = newEditedNoteVersion,
                    )
                }
            },
            onStateChange = { notesState.value = it },
        )

        Location.ARCHIVE -> ArchivePage(
            state = archiveState.value,
            modifier = Modifier.fillMaxSize(),
            compact = compact,
            onStateChange = { archiveState.value = it },
            onUnarchiveNote = { unarchivedNote ->
                unarchivedNote.archivedAt = null

                archiveState.value = archiveState.value.copy(
                    notes = archiveState.value.notes - unarchivedNote,
                    viewedNote =
                    if (archiveState.value.viewedNote == unarchivedNote) null
                    else archiveState.value.viewedNote
                )
                notesState.value = notesState.value.copy(
                    notes = notesState.value.notes + unarchivedNote
                )
            }
        )
    }

}