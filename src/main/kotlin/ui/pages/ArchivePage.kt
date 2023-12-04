package ui.pages

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Notes
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import entity.Note
import ui.navigation.ActivePane
import ui.AppTheme
import ui.data.state.ArchivePageState
import ui.data.stub.getExampleArchivedNote
import ui.data.stub.getExampleArchivedNotes
import ui.widgets.NoteList
import ui.widgets.NoteView

@Composable
fun ArchivePage(
    state: ArchivePageState,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    onBack: () -> Unit = {},
    onDeleteNote: (Note) -> Unit = {},
    onUnarchiveNote: (Note) -> Unit = {},
    onViewNote: (Note) -> Unit = {}
) {
    if (state.notes.isEmpty()) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Outlined.Archive,
                contentDescription = "Архив",
                modifier = Modifier.size(120.dp),
                tint = Color(229, 229, 229),
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Здесь будут храниться архивированные заметки.",
                color = Color(95, 99, 104),
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp,
            )
        }
    } else {
        if (compact) {
            when (state.activePane) {
                ActivePane.LIST -> {
                    NoteList(
                        state.notes,
                        modifier = Modifier.fillMaxSize(),
                        compact = compact,
                        onItemClick = onViewNote
                    )
                }
                ActivePane.VIEW -> {
                    NoteView(
                        note = state.viewedNote!!,
                        modifier = Modifier.fillMaxSize(),
                        compact = compact,
                        onBack = onBack,
                        onToggleArchivedState = { onUnarchiveNote(state.viewedNote) },
                    )
                }
            }
        } else {
            Row(modifier = Modifier.fillMaxSize()) {
                NoteList(
                    state.notes,
                    modifier = Modifier.fillMaxHeight().fillMaxWidth(0.4f),
                    compact = compact,
                    onItemClick = onViewNote
                )
                if (state.viewedNote != null) {
                    NoteView(
                        note = state.viewedNote,
                        modifier = Modifier.fillMaxSize(),
                        compact = compact,
                        onBack = onBack,
                        onDelete = { onDeleteNote(state.viewedNote) },
                        onToggleArchivedState = { onUnarchiveNote(state.viewedNote) },
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
private fun ArchivePageEmptyPreview() {
    AppTheme {
        ArchivePage(
            state = ArchivePageState(
                activePane = ActivePane.LIST,
                notes = emptyList(),
                viewedNote = null
            ),
            modifier = Modifier.fillMaxSize(),
        )
    }
}
@Composable
@Preview
private fun ArchivePageFilledCompactListPreview() {
    AppTheme {
        ArchivePage(
            state = ArchivePageState(
                activePane = ActivePane.LIST,
                notes = getExampleArchivedNotes(),
                viewedNote = getExampleArchivedNote()
            ),
            modifier = Modifier.fillMaxSize(),
            compact = true
        )
    }
}
@Composable
@Preview
private fun ArchivePageFilledCompactViewPreview() {
    AppTheme {
        ArchivePage(
            state = ArchivePageState(
                activePane = ActivePane.VIEW,
                notes = getExampleArchivedNotes(),
                viewedNote = getExampleArchivedNote()
            ),
            modifier = Modifier.fillMaxSize(),
            compact = true
        )
    }
}

@Composable
@Preview
private fun ArchivePageFilledPreview() {
    AppTheme {
        ArchivePage(
            state = ArchivePageState(
                activePane = ActivePane.LIST,
                notes = getExampleArchivedNotes(),
                viewedNote = getExampleArchivedNote()
            ),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun ArchivePageFilledWithoutSelectedPreview() {
    AppTheme {
        ArchivePage(
            state = ArchivePageState(
                activePane = ActivePane.LIST,
                notes = getExampleArchivedNotes(),
                viewedNote = null
            ),
            modifier = Modifier.fillMaxSize(),
        )
    }
}