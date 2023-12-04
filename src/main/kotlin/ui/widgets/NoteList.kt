package ui.widgets

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import entity.Note
import entity.NoteDraft
import ui.AppTheme
import ui.data.stub.getExampleNote

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteList(
    notes: List<Note>,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    allowedCreateNew: Boolean = false,
    draft: NoteDraft? = null,
    onCreateNew: () -> Unit = {},
    onDraftClick: () -> Unit = {},
    onItemClick: (Note) -> Unit = {},
) {
    val listState = rememberLazyListState()

    Scaffold(
        modifier = modifier,
        topBar = {
            if (allowedCreateNew && draft == null) {
                TopAppBar(
                    title = {},
                    navigationIcon = {},
                    actions = {
                        Tooltip(tooltip = "Добавить заметку") {
                            IconButton(onClick = onCreateNew) {
                                Icon(Icons.Default.Add, "Добавить заметку")
                            }
                        }
                    },
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            if (draft != null && draft.id == null) {
                NewNoteItem(
                    draft,
                    modifier = Modifier
                        .clickable { onDraftClick() }
                        .padding(
                            start = 10.dp, end = if (compact) 10.dp else 22.dp,
                            bottom = 10.dp, top = 10.dp
                        ),
                    compact = compact
                )
            }
            Box(
                modifier = Modifier.fillMaxSize().padding(10.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                        .padding(end = if (compact) 0.dp else 12.dp),
                    state = listState
                ) {
                    items(notes) { note ->
                        NoteListItem(
                            note = note,
                            modifier = Modifier.clickable { onItemClick(note) },
                            compact = compact,
                            hasDraft = note.id == draft?.id
                        )
                    }
                }
                if (!compact) {
                    VerticalScrollbar(
                        modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                        adapter = rememberScrollbarAdapter(listState)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun NoteListPreview() {
    AppTheme {
        NoteList(
            listOf(
                getExampleNote(),
                getExampleNote(),
            ),
            modifier = Modifier.fillMaxSize(),
        )
    }
}