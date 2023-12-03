package ui.data.state

import entity.Note
import navigation.ActivePane

data class ArchivePageState(
    val activePane: ActivePane,
    val notes: List<Note>,
    val viewedNote: Note?
)
