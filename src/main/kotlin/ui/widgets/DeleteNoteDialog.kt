package ui.widgets

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
@Preview
fun DeleteNoteDialog(
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    AlertDialog(
        icon = {
            Icon(Icons.Default.Dangerous, contentDescription = "Необратимое действие")
        },
        title = {
            Text(text = "Удаление заметки")
        },
        text = {
            Text(text = "Удалённую заметку невозможно восстановить. Вы точно хотите удалить заметку?")
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Удалить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}