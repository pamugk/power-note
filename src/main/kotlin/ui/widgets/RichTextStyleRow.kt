package ui.widgets

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.RichTextState

private data class TextSpanStyle(
    val name: String,
    val style: TextStyle
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RichTextStyleRow(
    modifier: Modifier = Modifier,
    state: RichTextState,
) {
    val selectedText = !state.selection.collapsed
    var showLinkDialog by remember { mutableStateOf(false) }
    var textStylesExpanded by remember { mutableStateOf(false) }
    val textStyles = listOf(
        TextSpanStyle(
            "Обычный текст",
            MaterialTheme.typography.bodyLarge
        ),
        TextSpanStyle(
            "Заголовок 1",
            MaterialTheme.typography.headlineLarge
        ),
        TextSpanStyle(
            "Заголовок 2",
            MaterialTheme.typography.headlineMedium
        ),
        TextSpanStyle(
            "Заголовок 3",
            MaterialTheme.typography.headlineSmall
        ),
        TextSpanStyle(
            "Заголовок 4",
            MaterialTheme.typography.titleLarge
        ),
        TextSpanStyle(
            "Заголовок 5",
            MaterialTheme.typography.titleMedium
        ),
        TextSpanStyle(
            "Заголовок 6",
            MaterialTheme.typography.titleSmall
        ),
    )
    val textStylesSelected = textStyles.firstOrNull {
        it.style.toSpanStyle() == state.currentSpanStyle
    } ?: textStyles[0]

    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        item {
            ExposedDropdownMenuBox(
                expanded = textStylesExpanded,
                onExpandedChange = {
                    textStylesExpanded = selectedText && it
                }
            ) {
                TextField(
                    value = textStylesSelected.name,
                    onValueChange = {},
                    modifier = Modifier.focusProperties { canFocus = false }.menuAnchor().width(IntrinsicSize.Min),
                    enabled = selectedText,
                    readOnly = true,
                    label = { Text("Стиль") },
                    trailingIcon = {
                        if (selectedText) {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = textStylesExpanded)
                        }
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                )
                ExposedDropdownMenu(
                    expanded = textStylesExpanded,
                    onDismissRequest = { textStylesExpanded = false },
                    modifier = Modifier.focusProperties { canFocus = false }
                ) {
                    textStyles.forEach { textStyleOption ->
                        DropdownMenuItem(
                            text = { Text(textStyleOption.name, style = textStyleOption.style) },
                            onClick = {
                                state.toggleSpanStyle(textStyleOption.style.toSpanStyle())
                                textStylesExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
        }

        item {
            EditorButton(
                onClick = { state.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold)) },
                isSelected = state.currentSpanStyle.fontWeight == FontWeight.Bold,
                icon = Icons.Outlined.FormatBold
            )
        }
        item {
            EditorButton(
                onClick = { state.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic)) },
                isSelected = state.currentSpanStyle.fontStyle == FontStyle.Italic,
                icon = Icons.Outlined.FormatItalic
            )
        }
        item {
            EditorButton(
                onClick = { state.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline)) },
                isSelected = state.currentSpanStyle.textDecoration?.contains(TextDecoration.Underline) == true,
                icon = Icons.Outlined.FormatUnderlined
            )
        }
        item {
            EditorButton(
                onClick = { state.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) },
                isSelected = state.currentSpanStyle.textDecoration?.contains(TextDecoration.LineThrough) == true,
                icon = Icons.Outlined.FormatStrikethrough
            )
        }

        item {
            Divider(Modifier.height(16.dp).width(1.dp))
        }

        item {
            EditorButton(
                onClick = { state.toggleUnorderedList() },
                isSelected = state.isUnorderedList,
                icon = Icons.Outlined.FormatListBulleted
            )
        }
        item {
            EditorButton(
                onClick = { state.toggleOrderedList() },
                isSelected = state.isOrderedList,
                icon = Icons.Outlined.FormatListNumbered
            )
        }

        item {
            Divider(Modifier.height(16.dp).width(1.dp))
        }

        item {
            EditorButton(
                onClick = {
                    if (!state.isLink) {
                        showLinkDialog = true
                    }
                },
                isSelected = state.isLink,
                icon = Icons.Outlined.AddLink
            )
        }
    }

    if (showLinkDialog) {
        LinkDialog(
            onConfirm = { createdLink ->
                state.addLink(createdLink.text, createdLink.link)
                showLinkDialog = false
            },
            onDismiss = {
                showLinkDialog = false
            }
        )
    }
}