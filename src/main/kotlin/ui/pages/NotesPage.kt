package ui.pages

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import entity.Note
import entity.NoteData
import ui.AppTheme

@Composable
fun NotesPage(
    modifier: Modifier = Modifier,
    notes: List<Note> = emptyList(),
    onArchiveNote: (Note) -> Unit = {},
    onCreateNote: (NoteData) -> Unit = {},
    onSaveNote: (Note, NoteData) -> Unit = { _, _ -> },
) {
    if (notes.isEmpty()) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Outlined.Lightbulb,
                contentDescription = "Заметки",
                modifier = Modifier.size(120.dp),
                tint = Color(229, 229, 229),
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Здесь будут ваши заметки.",
                color = Color(95, 99, 104),
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp,
            )
        }
    } else {

    }
}

@Composable
@Preview
private fun NotesPagePreview() {
    AppTheme {
        NotesPage(modifier = Modifier.fillMaxSize())
    }
}