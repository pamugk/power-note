package ui.data.state

import entity.Note
import ui.navigation.ActivePane

data class ArchivePageState(
    val activePane: ActivePane,
    val notes: List<Note>,
    val viewedNote: Note?
)
