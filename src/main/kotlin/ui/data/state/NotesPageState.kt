package ui.data.state

import entity.Note
import entity.NoteDraft
import ui.navigation.ActivePane

data class NotesPageState(
    val activePane: ActivePane,
    val draft: NoteDraft?,
    val notes: List<Note>,
    val viewedNote: Note?
)