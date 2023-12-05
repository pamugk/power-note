package data.repository

import data.state.DatasetState
import entity.Note
import entity.NoteDraft
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import java.util.concurrent.atomic.AtomicLong

class InMemoryNoteRepository: NoteRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var lastUsedId = AtomicLong(0L)
    private val notes = MutableStateFlow<Map<Long, Note>>(emptyMap())
    @OptIn(ExperimentalCoroutinesApi::class)
    private val processedNotes = notes.mapLatest { allNotes ->
        val (archivedNotes, notes) = allNotes.values.sortedByDescending { it.createdAt }.partition { it.archived }
        DatasetState(archivedNotes, notes)
    }.stateIn(coroutineScope, SharingStarted.Eagerly, DatasetState(emptyList(), emptyList()))

    override suspend fun archiveNote(archivedNoteId: Long) {
        withContext(Dispatchers.IO) {
            notes.update { oldNotes ->
                oldNotes[archivedNoteId]?.let { oldArchivedNote ->
                    oldNotes - archivedNoteId + Pair(archivedNoteId, oldArchivedNote.copy(archivedAt = Clock.System.now()))
                } ?: oldNotes
            }
        }
    }

    override suspend fun deleteNote(deletedNoteId: Long) {
        withContext(Dispatchers.IO) {
            notes.update { oldNotes ->
                oldNotes - deletedNoteId
            }
        }
    }

    override fun getNotes(): StateFlow<DatasetState> = processedNotes

    override suspend fun saveDraft(savedDraft: NoteDraft) {
        withContext(Dispatchers.IO) {
            notes.update { oldNotes ->
                if (savedDraft.id == null) {
                    val newNoteState = Note(
                        id = lastUsedId.getAndIncrement(),
                        createdAt = Clock.System.now(),
                        header = savedDraft.header,
                        content = savedDraft.content
                    )
                    oldNotes + Pair(newNoteState.id, newNoteState)
                }
                else {
                    oldNotes[savedDraft.id]?.let { oldEditedNote ->
                        oldNotes - savedDraft.id + Pair(savedDraft.id, oldEditedNote.copy(
                            lastUpdatedAt = Clock.System.now(),
                            header = savedDraft.header,
                            content = savedDraft.content
                        ))
                    } ?: oldNotes
                }
            }
        }
    }

    override suspend fun unarchiveNote(unarchivedNoteId: Long) {
        withContext (Dispatchers.IO) {
            notes.update { oldNotes ->
                oldNotes[unarchivedNoteId]?.let { oldUnarchivedNote ->
                    oldNotes - unarchivedNoteId + Pair(unarchivedNoteId, oldUnarchivedNote.copy(archivedAt = null))
                } ?: oldNotes
            }
        }
    }
}