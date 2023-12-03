package ui.widgets

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import entity.NoteDraft
import ui.AppTheme
import ui.data.stub.getExampleNewNote
import ui.utils.spaceNormalizationRegex

@Composable
fun NewNoteItem(
    noteDraft: NoteDraft,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
) {
    val contentPreview = noteDraft.content.replace(spaceNormalizationRegex, " ")

    OutlinedCard(
        modifier = modifier,
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = noteDraft.header,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            },
            overlineContent = {
                Text("Новая заметка")
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
private fun NewNoteItemPreview() {
    AppTheme {
        NewNoteItem(
            noteDraft = getExampleNewNote()
        )
    }
}