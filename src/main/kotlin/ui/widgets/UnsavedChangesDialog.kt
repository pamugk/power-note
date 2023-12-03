package ui.widgets

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable


@Composable
@Preview
fun UnsavedChangesDialog(
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    AlertDialog(
        icon = {
            Icon(Icons.Default.Warning, contentDescription = "Предупреждение")
        },
        title = {
            Text(text = "Несохранённые изменения")
        },
        text = {
            Text(text = "Вы точно хотите прервать редактирование заметки? Все несохранённые изменения будут утеряны.")
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Прервать")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}