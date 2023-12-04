package ui.pages

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Notes
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
import entity.NoteDraft
import ui.navigation.ActivePane
import ui.AppTheme
import ui.data.state.NotesPageState
import ui.data.stub.getExampleNewNote
import ui.data.stub.getExampleNote
import ui.data.stub.getExampleNotes
import ui.widgets.NoteEditor
import ui.widgets.NoteList
import ui.widgets.NoteView
import ui.widgets.Tooltip

@Composable
fun NotesPage(
    state: NotesPageState,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    onArchiveNote: (Note) -> Unit = {},
    onBack: () -> Unit = {},
    onEdit: (NoteDraft) -> Unit = {},
    onResetChanges: () -> Unit = {},
    onSaveNote: (NoteDraft) -> Unit = {},
    onStartEditing: (Note?) -> Unit = {},
    onViewNewNoteDraft: () -> Unit = {},
    onViewNote: (Note) -> Unit = {}
) {
    if (state.notes.isEmpty() && state.draft == null) {
        Scaffold(
            modifier = modifier,
            floatingActionButton = {
                Tooltip(
                    tooltip = "Добавить заметку"
                ) {
                    FloatingActionButton(
                        onClick = { onStartEditing(null) },
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
        if (compact) {
            when (state.activePane) {
                ActivePane.LIST -> {
                    NoteList(
                        state.notes,
                        modifier = Modifier.fillMaxSize(),
                        compact = compact,
                        allowedCreateNew = true,
                        draft = state.draft,
                        onCreateNew = { onStartEditing(null) },
                        onDraftClick = onViewNewNoteDraft,
                        onItemClick = onViewNote
                    )
                }
                ActivePane.VIEW -> {
                    if (state.draft != null && state.viewedNote?.id == state.draft.id) {
                        NoteEditor(
                            noteDraft = state.draft,
                            modifier = Modifier.fillMaxSize(),
                            compact = compact,
                            onBack = onBack,
                            onResetChanges = onResetChanges,
                            onEdit = onEdit,
                            onSave = { onSaveNote(state.draft) },
                        )
                    } else if (state.viewedNote != null) {
                        NoteView(
                            note = state.viewedNote,
                            modifier = Modifier.fillMaxSize(),
                            compact = compact,
                            editInProcess = state.draft != null,
                            onBack = onBack,
                            onStartEditing = { onStartEditing(state.viewedNote) },
                            onToggleArchivedState = { onArchiveNote(state.viewedNote) },
                        )
                    }
                }
            }
        } else {
            Row(modifier = Modifier.fillMaxSize()) {
                NoteList(
                    state.notes,
                    modifier = Modifier.fillMaxHeight().fillMaxWidth(0.4f),
                    compact = compact,
                    allowedCreateNew = true,
                    draft = state.draft,
                    onCreateNew = { onStartEditing(null) },
                    onDraftClick = onViewNewNoteDraft,
                    onItemClick = onViewNote
                )
                if (state.draft != null && state.viewedNote?.id == state.draft.id) {
                    NoteEditor(
                        noteDraft = state.draft,
                        modifier = Modifier.fillMaxSize(),
                        compact = compact,
                        onBack = onBack,
                        onResetChanges = onResetChanges,
                        onEdit = onEdit,
                        onSave = { onSaveNote(state.draft) },
                    )
                } else if (state.viewedNote != null) {
                    NoteView(
                        note = state.viewedNote,
                        modifier = Modifier.fillMaxSize(),
                        compact = compact,
                        editInProcess = state.draft != null,
                        onBack = onBack,
                        onStartEditing = { onStartEditing(state.viewedNote) },
                        onToggleArchivedState = { onArchiveNote(state.viewedNote) },
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notes,
                            contentDescription = "Место для отображения заметки",
                            modifier = Modifier.size(120.dp),
                            tint = Color(229, 229, 229),
                        )
                        Spacer(Modifier.height(20.dp))
                        Text(
                            text = "Здесь будут отображаться данные выбранной заметки.",
                            color = Color(95, 99, 104),
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 28.sp,
                        )
                    }
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
            state = NotesPageState(
                activePane = ActivePane.LIST,
                draft = null,
                notes = emptyList(),
                viewedNote = null,
            ),
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
                activePane = ActivePane.LIST,
                draft = null,
                notes = getExampleNotes(),
                viewedNote = null,
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
            state = NotesPageState(
                activePane = ActivePane.LIST,
                draft = null,
                notes = getExampleNotes(),
                viewedNote = null,
            ),
            modifier = Modifier.fillMaxSize(),
            compact = true,
        )
    }
}

@Composable
@Preview
private fun NotesPageFirstNotePreview() {
    AppTheme {
        NotesPage(
            state = NotesPageState(
                activePane = ActivePane.VIEW,
                draft = getExampleNewNote(),
                notes = emptyList(),
                viewedNote = null,
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
                activePane = ActivePane.VIEW,
                draft = null,
                notes = getExampleNotes(),
                viewedNote = getExampleNote(),
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
                activePane = ActivePane.LIST,
                draft = null,
                notes = getExampleNotes(),
                viewedNote = getExampleNote(),
            ),
            modifier = Modifier.fillMaxSize(),
        )
    }
}