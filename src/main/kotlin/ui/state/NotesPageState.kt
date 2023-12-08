package ui.state

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import entity.Note
import ui.navigation.ActivePane

class NotesPageState(
    val draft: NoteDraftState = NoteDraftState(),
    initialActivePane: ActivePane = ActivePane.LIST,
    initialNotes: List<Note> = emptyList(),
    initialViewedNote: Note? = null
) {
    private val _viewedNote = mutableStateOf(initialViewedNote)

    val activePane = mutableStateOf(initialActivePane)
    val notes = mutableStateOf(initialNotes)
    val viewedNote: State<Note?>
        get() = _viewedNote

    fun goBack() {
        activePane.value = ActivePane.LIST
        _viewedNote.value = null
    }

    fun viewNote(note: Note) {
        _viewedNote.value = note
        activePane.value = ActivePane.VIEW
    }
}