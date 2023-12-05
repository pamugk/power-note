package data.state

import entity.Note

data class DatasetState(
    val archivedNotes: List<Note>,
    val notes: List<Note>
)
