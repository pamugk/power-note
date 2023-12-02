package ui.pages

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
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
import ui.AppTheme

@Composable
fun ArchivePage(
    modifier: Modifier = Modifier,
    archivedNotes: List<Note> = emptyList(),
    onUnarchiveNote: (Note) -> Unit = {},
) {
    if (archivedNotes.isEmpty()) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Outlined.Archive,
                contentDescription = "Архив",
                modifier = Modifier.size(120.dp),
                tint = Color(229, 229, 229),
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Здесь будут храниться архивированные заметки.",
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
private fun ArchivePagePreview() {
    AppTheme {
        ArchivePage(modifier = Modifier.fillMaxSize())
    }
}