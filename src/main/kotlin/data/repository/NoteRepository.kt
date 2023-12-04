package data.repository

import entity.Note
import entity.NoteDraft
import kotlinx.coroutines.flow.StateFlow

interface NoteRepository {
    suspend fun archiveNote(archivedNote: Note)

    suspend fun deleteNote(deletedNote: Note)

    fun getNotes(): StateFlow<Pair<List<Note>, List<Note>>>

    suspend fun saveDraft(savedDraft: NoteDraft)

    suspend fun unarchiveNote(unarchivedNote: Note)
}