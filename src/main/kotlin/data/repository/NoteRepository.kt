package data.repository

import data.state.DatasetState
import entity.NoteDraft
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Instant

interface NoteRepository {
    suspend fun archiveNote(archivedNoteId: Long)

    suspend fun deleteNote(deletedNoteId: Long)

    fun getNotes(): StateFlow<DatasetState>

    suspend fun runCleanup(threshold: Instant)

    suspend fun saveDraft(savedDraft: NoteDraft)

    suspend fun unarchiveNote(unarchivedNoteId: Long)
}