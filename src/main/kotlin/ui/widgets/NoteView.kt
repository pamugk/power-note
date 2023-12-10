package ui.widgets

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import entity.Note
import ui.AppTheme
import ui.data.stub.getExampleArchivedNote
import ui.data.stub.getExampleNote

private enum class ShownDialogOnView {
    NONE,
    ARCHIVE,
    DELETE,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteView(
    note: Note,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    editInProcess: Boolean = false,
    onBack: () -> Unit = {},
    onDelete: () -> Unit = {},
    onStartEditing: () -> Unit = {},
    onToggleArchivedState: () -> Unit = {},
) {
    val displayedText by remember {
        derivedStateOf {
            RichTextState().apply {
                if (note.styledContent == null) {
                    setText(note.content)
                } else {
                    setHtml(note.styledContent)
                }
            }
        }
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var shownDialog by remember { mutableStateOf(ShownDialogOnView.NONE) }
    val textScrollState = rememberScrollState(0)

    when (shownDialog) {
        ShownDialogOnView.NONE -> {}
        ShownDialogOnView.ARCHIVE -> {
            ArchiveNoteDialog(
                onConfirm = {
                    onToggleArchivedState()
                },
                onDismiss = {
                    shownDialog = ShownDialogOnView.NONE
                }
            )
        }
        ShownDialogOnView.DELETE -> {
            DeleteNoteDialog(
                onConfirm = {
                    onDelete()
                },
                onDismiss = {
                    shownDialog = ShownDialogOnView.NONE
                }
            )
        }
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        text = note.header,
                        modifier = Modifier.padding(end = 12.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    Tooltip(tooltip = "Назад") {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, "Назад")
                        }
                    }
                },
                actions = {
                    if (note.archived) {
                        Tooltip(tooltip = "Вернуть из архива") {
                            IconButton(onClick = onToggleArchivedState) {
                                Icon(Icons.Default.Unarchive, "Вернуть из архива")
                            }
                        }
                    } else {
                        Tooltip(tooltip = "Архивировать") {
                            IconButton(
                                onClick = {
                                    shownDialog = ShownDialogOnView.ARCHIVE
                                }
                            ) {
                                Icon(Icons.Default.Archive, "Архивировать")
                            }
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = { },
                floatingActionButton = {
                    if (!note.archived && !editInProcess) {
                        Tooltip(tooltip = "Редактировать") {
                            FloatingActionButton(
                                onClick = onStartEditing,
                            ) {
                                Icon(Icons.Default.Edit, "Редактировать")
                            }
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            RichText(
                state = displayedText,
                modifier = Modifier.fillMaxSize()
                    .padding(start = 16.dp, end = if (compact) 16.dp else 28.dp)
                    .verticalScroll(textScrollState)
            )
            if (!compact) {
                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(textScrollState)
                )
            }
        }
    }
}

@Composable
@Preview
private fun NoteViewPreview() {
    AppTheme {
        NoteView(
            note = getExampleNote(),
            modifier = Modifier.fillMaxSize(),
            compact = true,
        )
    }
}

@Composable
@Preview
private fun NoteViewArchivedPreview() {
    AppTheme {
        NoteView(
            note = getExampleArchivedNote(),
            modifier = Modifier.fillMaxSize(),
            compact = true,
        )
    }
}