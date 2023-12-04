package data.repository

import entity.Note
import entity.NoteDraft
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import java.util.concurrent.atomic.AtomicLong

private infix fun <T> List<T>.prepend(e: T): List<T> {
    return buildList(this.size + 1) {
        add(e)
        addAll(this@prepend)
    }
}

class InMemoryNoteRepository: NoteRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var lastUsedId = AtomicLong(0L)
    private val notes = MutableStateFlow<List<Note>>(emptyList())
    @OptIn(ExperimentalCoroutinesApi::class)
    private val processedNotes = notes.mapLatest { allNotes ->
        allNotes.sortedByDescending { it.createdAt }.partition { it.archived }
    }.stateIn(coroutineScope, SharingStarted.Eagerly, Pair(emptyList(), emptyList()))

    override suspend fun archiveNote(archivedNote: Note) {
        withContext(Dispatchers.IO) {
            val newArchivedNote = archivedNote.copy(archivedAt = Clock.System.now())
            notes.update { oldNotes ->
                if (oldNotes.any { it.id == archivedNote.id })
                    (oldNotes.filterNot { it.id == archivedNote.id } + newArchivedNote)
                else oldNotes
            }
        }
    }

    override suspend fun deleteNote(deletedNote: Note) {
        withContext(Dispatchers.IO) {
            notes.update { oldNotes ->
                oldNotes.filterNot { it.id == deletedNote.id }
            }
        }
    }

    override fun getNotes(): StateFlow<Pair<List<Note>, List<Note>>> = processedNotes

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
                    oldNotes prepend newNoteState
                }
                else {
                    oldNotes.find { it.id == savedDraft.id }?.let { oldEditedNote ->
                        val newEditedNote = oldEditedNote.copy(
                            lastUpdatedAt = Clock.System.now(),
                            header = savedDraft.header,
                            content = savedDraft.content
                        )
                        (oldNotes.filterNot { it.id == savedDraft.id } + newEditedNote)
                    } ?: oldNotes
                }
            }
        }
    }

    override suspend fun unarchiveNote(unarchivedNote: Note) {
        withContext (Dispatchers.IO) {
            val newUnarchivedNote = unarchivedNote.copy(archivedAt = null)
            notes.update { oldNotes ->
                if (oldNotes.any { it.id == unarchivedNote.id })
                    (oldNotes.filterNot { it.id == unarchivedNote.id } + newUnarchivedNote)
                else oldNotes
            }
        }
    }
}