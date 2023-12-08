package ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun EditorButton(
    onClick: () -> Unit,
    icon: ImageVector,
    isSelected: Boolean = false,
) {
    IconButton(
        modifier = Modifier.focusProperties { canFocus = false },
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onBackground
            },
        ),
    ) {
        Icon(
            icon,
            contentDescription = icon.name,
            modifier = Modifier
                .background(
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Transparent
                    },
                    shape = CircleShape
                )
        )
    }
}