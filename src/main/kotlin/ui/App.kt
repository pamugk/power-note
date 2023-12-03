package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import entity.Note
import navigation.Location
import ui.pages.ArchivePage
import ui.pages.NotesPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    var location by remember { mutableStateOf(Location.MAIN) }
    var viewedArchivedNote by remember { mutableStateOf<Note?>(null) }
    var viewedNote by remember { mutableStateOf<Note?>(null) }

    val archivedNotes = mutableStateListOf<Note>()
    val notes = mutableStateListOf<Note>()

    AppTheme {
        Scaffold() { innerPadding ->
            PermanentNavigationDrawer(
                drawerContent = {
                    PermanentDrawerSheet(Modifier.width(IntrinsicSize.Min)) {
                        Spacer(Modifier.height(12.dp))
                        NavigationDrawerItem(
                            label = { Text("Заметки") },
                            selected = location == Location.MAIN,
                            onClick = { location = Location.MAIN },
                            icon = { Icon(Icons.Outlined.Lightbulb, "Заметки") }
                        )
                        NavigationDrawerItem(
                            label = { Text("Архив") },
                            selected = location == Location.ARCHIVE,
                            onClick = { location = Location.ARCHIVE },
                            icon = { Icon(Icons.Outlined.Archive, "Архив") }
                        )
                    }
                },
                modifier = Modifier.fillMaxHeight().padding(innerPadding),
            ) {
                when (location) {
                    Location.MAIN -> NotesPage(
                        modifier = Modifier.fillMaxSize(),
                    )
                    Location.ARCHIVE -> ArchivePage(
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}