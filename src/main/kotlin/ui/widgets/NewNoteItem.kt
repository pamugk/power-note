package ui.widgets

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import ui.AppTheme
import ui.data.stub.getExampleNewNote
import ui.state.NoteDraftState
import ui.utils.spaceNormalizationRegex

@Composable
fun NewNoteItem(
    compact: Boolean = false,
    modifier: Modifier = Modifier,
    noteDraft: NoteDraftState
) {
    val contentPreview = noteDraft.content.annotatedString.replace(spaceNormalizationRegex, " ")

    OutlinedCard(
        modifier = modifier,
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = noteDraft.header.value,
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
            noteDraft = NoteDraftState(getExampleNewNote())
        )
    }
}