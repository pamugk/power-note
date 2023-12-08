package ui.state

import androidx.compose.runtime.mutableStateOf
import entity.Note

class ArchivePageState(
    initialNotes: List<Note> = emptyList(),
    initialViewedNote: Note? = null,
    private val onDelete: (Note) -> Unit = {},
    private val onUnarchive: (Note) -> Unit = {},
) {
    val notes = mutableStateOf(initialNotes)
    val viewedNote = mutableStateOf(initialViewedNote)

    fun deleteNote(note: Note) = onDelete(note)

    fun unarchiveNote(note: Note) = onUnarchive(note)
}