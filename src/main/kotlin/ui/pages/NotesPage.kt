package ui.pages

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import entity.Note
import ui.AppTheme
import ui.data.stub.getExampleNewNote
import ui.data.stub.getExampleNote
import ui.data.stub.getExampleNotes
import ui.navigation.ActivePane
import ui.state.NoteDraftState
import ui.state.NotesPageState
import ui.widgets.NoteEditor
import ui.widgets.NoteList
import ui.widgets.NoteView
import ui.widgets.Tooltip

@Composable
fun NotesPage(
    compact: Boolean = false,
    modifier: Modifier = Modifier,
    onArchiveNote: (Note) -> Unit = {},
    state: NotesPageState
) {
    if (state.notes.value.isEmpty() && !state.draft.inProcess.value) {
        Scaffold(
            modifier = modifier,
            floatingActionButton = {
                Tooltip(
                    tooltip = "Добавить заметку"
                ) {
                    FloatingActionButton(
                        onClick = { state.draft.startEditing(null) },
                    ) {
                        Icon(Icons.Default.Add, "Добавить заметку")
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Lightbulb,
                    contentDescription = "Заметки",
                    modifier = Modifier.size(120.dp),
                    tint = Color(229, 229, 229),
                )
                Spacer(Modifier.height(20.dp))
                Text(
                    text = "Здесь будут ваши заметки.",
                    color = Color(95, 99, 104),
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp,
                )
            }
        }
    } else {
        when (state.activePane.value) {
            ActivePane.LIST -> {
                NoteList(
                    state.notes.value,
                    modifier = modifier,
                    compact = compact,
                    allowedCreateNew = true,
                    draft = state.draft,
                    onCreateNew = { state.draft.startEditing(null) },
                    onDraftClick = { state.activePane.value = ActivePane.VIEW },
                    onItemClick = state::viewNote,
                    searchText = state.searchText
                )
            }
            ActivePane.VIEW -> {
                val viewedNote = state.viewedNote.value
                if (state.draft.editingNote(viewedNote)) {
                    NoteEditor(
                        compact = compact,
                        modifier = modifier,
                        onBack = state::goBack,
                        state = state.draft
                    )
                } else if (viewedNote != null) {
                    NoteView(
                        note = viewedNote,
                        modifier = modifier,
                        compact = compact,
                        editInProcess = state.draft.inProcess.value,
                        onBack = state::goBack,
                        onStartEditing = { state.draft.startEditing(viewedNote) },
                        onToggleArchivedState = { onArchiveNote(viewedNote) },
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun NotesPageEmptyPreview() {
    AppTheme {
        NotesPage(
            state = NotesPageState(),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
@Preview
private fun NotesPageFilledPreview() {
    AppTheme {
        NotesPage(
            state = NotesPageState(
                initialNotes = getExampleNotes(),
            ),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun NotesPageFilledCompactPreview() {
    AppTheme {
        NotesPage(
            compact = true,
            modifier = Modifier.fillMaxSize(),
            state = NotesPageState(
                initialNotes = getExampleNotes(),
            )
        )
    }
}

@Composable
@Preview
private fun NotesPageFirstNotePreview() {
    AppTheme {
        NotesPage(
            state = NotesPageState(
                draft = NoteDraftState(getExampleNewNote()),
                initialActivePane = ActivePane.VIEW
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
@Preview
private fun NotesPageViewCompactPreview() {
    AppTheme {
        NotesPage(
            state = NotesPageState(
                initialActivePane = ActivePane.VIEW,
                initialNotes = getExampleNotes(),
                initialViewedNote = getExampleNote(),
            ),
            modifier = Modifier.fillMaxSize(),
            compact = true,
        )
    }
}

@Composable
@Preview
private fun NotesPageViewPreview() {
    AppTheme {
        NotesPage(
            state = NotesPageState(
                initialNotes = getExampleNotes(),
                initialViewedNote = getExampleNote(),
            ),
            modifier = Modifier.fillMaxSize(),
        )
    }
}