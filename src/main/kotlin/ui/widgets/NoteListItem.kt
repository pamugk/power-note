package ui.widgets

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import entity.Note
import ui.AppTheme
import ui.data.stub.getExampleArchivedNote
import ui.data.stub.getExampleNote
import ui.utils.spaceNormalizationRegex

@Composable
fun NoteListItem(
    note: Note,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    hasDraft: Boolean = false
) {
    val contentPreview = note.content.replace(spaceNormalizationRegex, " ")

    OutlinedCard(
        modifier = modifier,
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = note.header,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            },
            overlineContent = {
                if (hasDraft) {
                    Text("Редактируется…")
                }
            },
            supportingContent = {
                Text(
                    text = contentPreview,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Justify,
                    maxLines = if (compact) 2 else 1,
                )
            }
        )
    }
}

@Composable
@Preview
private fun NoteListItemPreview() {
    AppTheme {
        NoteListItem(
            note = getExampleNote(),
        )
    }
}

@Composable
@Preview
private fun NoteListItemArchivedPreview() {
    AppTheme {
        NoteListItem(
            note = getExampleArchivedNote(),
        )
    }
}