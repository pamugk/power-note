package ui.widgets

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.AppTheme
import utils.isValidUrl

data class Link(
    val text: String,
    val link: String
)

@Composable
fun LinkDialog(
    initialValue: Link = Link("", ""),
    onConfirm: (Link) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    var link by remember { mutableStateOf(initialValue.link) }
    var text by remember { mutableStateOf(initialValue.text) }
    val validLink by remember { derivedStateOf { isValidUrl(link) } }

    AlertDialog(
        title = {
            Text(text = "Добавить ссылку")
        },
        text = {
            Column {
                TextField(
                    text,
                    onValueChange = { text = it },
                    label = { Text("Текст") }
                )
                Spacer(Modifier.height(8.dp))
                TextField(
                    link,
                    onValueChange = { link = it },
                    label = { Text("URL") }
                )
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { onConfirm(Link(text, link)) },
                enabled = text.isNotEmpty() && validLink
            ) {
                Text("Добавить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

@Composable
@Preview
private fun LinkDialogEmptyPreview() {
    AppTheme {
        LinkDialog()
    }
}

@Composable
@Preview
private fun LinkDialogFilledPreview() {
    AppTheme {
        LinkDialog(initialValue = Link("GitHub", "https://github.com/"))
    }
}