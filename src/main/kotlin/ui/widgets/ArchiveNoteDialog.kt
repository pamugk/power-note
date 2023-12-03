package ui.widgets

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable


@Composable
@Preview
fun ArchiveNoteDialog(
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    AlertDialog(
        icon = {
            Icon(Icons.Default.Info, contentDescription = "Информация к сведению")
        },
        title = {
            Text(text = "Архивирование заметки")
        },
        text = {
            Text(text = "Вы точно хотите архивировать заметку? Архивированную заметку можно восстановить не позднее чем спустя 7 дней после архивирования, по истечении срока она будет автоматически удалена.")
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Архивировать")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}