package ui.data.state

import ui.navigation.Location

data class AppState(
    val archivePageState: ArchivePageState,
    val currentLocation: Location,
    val notesPageState: NotesPageState
)
