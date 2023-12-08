package ui.pages

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.AppTheme
import ui.data.stub.getExampleArchivedNote
import ui.data.stub.getExampleArchivedNotes
import ui.state.ArchivePageState
import ui.widgets.NoteList
import ui.widgets.NoteView

@Composable
fun ArchivePage(
    compact: Boolean = false,
    modifier: Modifier = Modifier,
    state: ArchivePageState
) {
    if (state.notes.value.isEmpty()) {
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
        val viewedNote = state.viewedNote.value
        if (viewedNote == null) {
            NoteList(
                state.notes.value,
                modifier = modifier,
                compact = compact,
                onItemClick = { state.viewedNote.value = it }
            )
        } else {
            NoteView(
                note = viewedNote,
                modifier = modifier,
                compact = compact,
                onBack = { state.viewedNote.value = null },
                onDelete = { state.deleteNote(viewedNote) },
                onToggleArchivedState = { state.unarchiveNote(viewedNote) },
            )
        }
    }
}

@Composable
@Preview
private fun ArchivePageEmptyPreview() {
    AppTheme {
        ArchivePage(
            modifier = Modifier.fillMaxSize(),
            state = ArchivePageState()
        )
    }
}
@Composable
@Preview
private fun ArchivePageFilledCompactListPreview() {
    AppTheme {
        ArchivePage(
            compact = true,
            modifier = Modifier.fillMaxSize(),
            state = ArchivePageState(
                initialNotes = getExampleArchivedNotes(),
                initialViewedNote = getExampleArchivedNote()
            )
        )
    }
}
@Composable
@Preview
private fun ArchivePageFilledCompactViewPreview() {
    AppTheme {
        ArchivePage(
            compact = true,
            modifier = Modifier.fillMaxSize(),
            state = ArchivePageState(
                initialNotes = getExampleArchivedNotes(),
                initialViewedNote = getExampleArchivedNote()
            )
        )
    }
}

@Composable
@Preview
private fun ArchivePageFilledPreview() {
    AppTheme {
        ArchivePage(
            modifier = Modifier.fillMaxSize(),
            state = ArchivePageState(
                initialNotes = getExampleArchivedNotes(),
                initialViewedNote = getExampleArchivedNote()
            )
        )
    }
}

@Composable
@Preview
private fun ArchivePageFilledWithoutSelectedPreview() {
    AppTheme {
        ArchivePage(
            modifier = Modifier.fillMaxSize(),
            state = ArchivePageState(
                initialNotes = getExampleArchivedNotes()
            )
        )
    }
}