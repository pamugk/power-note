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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import entity.Note
import ui.AppTheme
import ui.data.stub.getExampleNote
import ui.state.NoteDraftState
import ui.utils.tokenize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteList(
    notes: List<Note>,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    allowedCreateNew: Boolean = false,
    draft: NoteDraftState = NoteDraftState(),
    onCreateNew: () -> Unit = {},
    onDraftClick: () -> Unit = {},
    onItemClick: (Note) -> Unit = {},
    searchText: MutableState<String> = remember { mutableStateOf("") }
) {
    val filteredNotes by remember {
        derivedStateOf {
            if (searchText.value.isBlank() || searchText.value.length < 3) {
                notes
            } else {
                val tokenizedQuery = tokenize(searchText.value)
                notes.filter { note ->
                    tokenizedQuery.all { token ->
                        note.header.contains(token, ignoreCase = true)
                            || note.content.contains(token, ignoreCase = true)
                    }

                }
            }
        }
    }
    val listState = rememberLazyListState()

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    TextField(
                        searchText.value,
                        onValueChange = { searchText.value = it },
                        modifier = Modifier.fillMaxWidth(if (compact) 0.9f else 0.75f).padding(vertical = 8.dp),
                        placeholder = {
                            Text("Поиск…")
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Search, "Поиск")
                        },
                        trailingIcon = {
                            if (searchText.value.isNotEmpty()) {
                                IconButton(onClick = { searchText.value = "" }) {
                                    Icon(Icons.Default.Clear, "Очистить")
                                }
                            }
                        },
                        singleLine = true
                    )
                },
                navigationIcon = {},
                actions = {
                    if (allowedCreateNew && !draft.inProcess.value) {
                        Tooltip(tooltip = "Добавить заметку") {
                            IconButton(onClick = onCreateNew) {
                                Icon(Icons.Default.Add, "Добавить заметку")
                            }
                        }
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            if (draft.editingNote(null)) {
                NewNoteItem(
                    compact = compact,
                    modifier = Modifier
                        .clickable { onDraftClick() }
                        .padding(
                            start = 10.dp, end = if (compact) 10.dp else 22.dp,
                            bottom = 10.dp, top = 10.dp
                        ),
                    noteDraft = draft
                )
            }
            if (filteredNotes.isNotEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(10.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                            .padding(end = if (compact) 0.dp else 12.dp),
                        state = listState
                    ) {
                        items(filteredNotes) { note ->
                            NoteListItem(
                                note = note,
                                modifier = Modifier.clickable { onItemClick(note) },
                                compact = compact,
                                hasDraft = draft.editingNote(note)
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
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.SearchOff,
                        contentDescription = "Ничего не найдено",
                        modifier = Modifier.size(120.dp),
                        tint = Color(229, 229, 229),
                    )
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text = "Ничего не найдено.",
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

@Composable
@Preview
private fun NoteListNotFoundPreview() {
    AppTheme {
        NoteList(
            listOf(
                getExampleNote(),
                getExampleNote(),
            ),
            modifier = Modifier.fillMaxSize(),
            searchText = mutableStateOf("Оченьдлиннаяпоисковаястрока")
        )
    }
}