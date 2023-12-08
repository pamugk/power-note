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
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import ui.AppTheme
import ui.data.stub.getExampleNewNote
import ui.state.NoteDraftState

private enum class ShownDialogOnEdit {
    NONE,
    UNSAVED_CHANGES,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditor(
    compact: Boolean = false,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    state: NoteDraftState,
) {
    var shownDialog by remember { mutableStateOf(ShownDialogOnEdit.NONE) }
    val textScrollState = rememberScrollState(0)

    when(shownDialog) {
        ShownDialogOnEdit.NONE -> {}
        ShownDialogOnEdit.UNSAVED_CHANGES -> {
            UnsavedChangesDialog(
                onConfirm = state::reset,
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
                        value = state.header.value,
                        onValueChange = { state.header.value = it },
                        modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
                        label = { Text("Заголовок") },
                        supportingText = {
                            Text(
                                text = "${state.header.value.length} / ${state.maxHeaderLength}",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                            )
                        },
                        isError = state.invalidHeader,
                        singleLine = true,
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
                    if (state.valid()) {
                        Tooltip(tooltip = "Сохранить") {
                            FloatingActionButton(
                                onClick = state::save,
                            ) {
                                Icon(Icons.Default.Save, "Сохранить")
                            }
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding)) {
            RichTextStyleRow(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                state = state.content,
            )
            Box(modifier = Modifier.fillMaxSize()) {
                RichTextEditor(
                    state = state.content,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(textScrollState),
                    label = { Text("Содержимое") },
                    supportingText = {
                        Text(
                            text = "${state.content.annotatedString.length} / ${state.maxContentLength}",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                        )
                    },
                    isError = state.invalidContent
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
}

@Composable
@Preview
private fun NoteEditorPreview() {
    AppTheme {
        NoteEditor(
            modifier = Modifier.fillMaxSize(),
            state = NoteDraftState(getExampleNewNote())
        )
    }
}