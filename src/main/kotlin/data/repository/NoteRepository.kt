package data.repository

import entity.Note
import entity.NoteDraft
import kotlinx.coroutines.flow.StateFlow

interface NoteRepository {
    suspend fun archiveNote(archivedNoteId: Long)

    suspend fun deleteNote(deletedNoteId: Long)

    fun getNotes(): StateFlow<Pair<List<Note>, List<Note>>>

    suspend fun saveDraft(savedDraft: NoteDraft)

    suspend fun unarchiveNote(unarchivedNoteId: Long)
}