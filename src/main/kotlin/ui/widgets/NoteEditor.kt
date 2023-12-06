package ui.widgets

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import entity.NoteDraft
import ui.AppTheme
import ui.data.stub.getExampleNewNote

private enum class ShownDialogOnEdit {
    NONE,
    UNSAVED_CHANGES,
}

private const val MAX_CONTENT_LENGTH = 20000
private const val MAX_HEADER_LENGTH = 1000

private fun draftIsValid(noteDraft: NoteDraft) =
    noteDraft.content.isNotEmpty() && noteDraft.content.length <= MAX_CONTENT_LENGTH
            && noteDraft.header.isNotEmpty() && noteDraft.header.length <= MAX_HEADER_LENGTH

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditor(
    noteDraft: NoteDraft,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    onBack: () -> Unit = {},
    onEdit: (NoteDraft) -> Unit = {},
    onResetChanges: () -> Unit = {},
    onSave: () -> Unit = {},
) {
    var shownDialog by remember { mutableStateOf(ShownDialogOnEdit.NONE) }

    val textScrollState = rememberScrollState(0)

    when(shownDialog) {
        ShownDialogOnEdit.NONE -> {}
        ShownDialogOnEdit.UNSAVED_CHANGES -> {
            UnsavedChangesDialog(
                onConfirm = {
                    onResetChanges()
                },
                onDismiss = {
                    shownDialog = ShownDialogOnEdit.NONE
                }
            )
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            LargeTopAppBar(
                title = {
                    TextField(
                        value = noteDraft.header,
                        onValueChange = { onEdit(noteDraft.copy(header = it)) },
                        modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
                        label = { Text("Заголовок") },
                        supportingText = {
                            Text(
                                text = "${noteDraft.header.length} / $MAX_HEADER_LENGTH",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                            )
                        },
                        isError = noteDraft.header.length > MAX_HEADER_LENGTH,
                        singleLine = true,
                    )
                },
                navigationIcon = {
                    if (compact) {
                        Tooltip(tooltip = "Назад") {
                            IconButton(onClick = onBack) {
                                Icon(Icons.Default.ArrowBack, "Назад")
                            }
                        }
                    }
                },
                actions = {
                    Tooltip(tooltip = "Прервать редактирование") {
                        IconButton(
                            onClick = {
                                shownDialog = ShownDialogOnEdit.UNSAVED_CHANGES
                            }
                        ) {
                            Icon(Icons.Default.Close, "Прервать редактирование")
                        }
                    }
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {},
                floatingActionButton = {
                    if (draftIsValid(noteDraft)) {
                        Tooltip(tooltip = "Сохранить") {
                            FloatingActionButton(
                                onClick = onSave,
                            ) {
                                Icon(Icons.Default.Save, "Сохранить")
                            }
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            TextField(
                value = noteDraft.content,
                onValueChange = { onEdit(noteDraft.copy(content = it)) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(textScrollState),
                label = { Text("Содержимое") },
                supportingText = {
                    Text(
                        text = "${noteDraft.content.length} / $MAX_CONTENT_LENGTH",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                    )
                },
                isError = noteDraft.content.length > MAX_CONTENT_LENGTH
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
private fun NoteEditorPreview() {
    AppTheme {
        NoteEditor(
            noteDraft = getExampleNewNote(),
            modifier = Modifier.fillMaxSize()
        )
    }
}