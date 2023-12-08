package ui.state

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import data.repository.NoteRepository
import entity.Note
import entity.NoteDraft
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import ui.navigation.ActivePane
import ui.navigation.Location
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class AppState(private val noteRepository: NoteRepository) {

    private val _initialized = mutableStateOf(false)

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val draftState: NoteDraftState = NoteDraftState(
        onReset = {
            if (notesState.viewedNote.value == null) {
                notesState.goBack()
            }
        },
        onSave = ::saveNoteDraft,
        onStartEditing = {
            notesState.activePane.value = ActivePane.VIEW
        }
    )

    val archivedNotesState = ArchivePageState(onDelete = ::deleteNote, onUnarchive = ::unarchiveNote)
    val currentLocation = mutableStateOf(Location.MAIN)
    val initialized: State<Boolean>
        get() = _initialized
    val notesState = NotesPageState(draft = draftState)

    fun archiveNote(archivedNote: Note) {
        coroutineScope.launch {
            noteRepository.archiveNote(archivedNote.id)
        }
    }

    private fun deleteNote(deletedNote: Note) {
        coroutineScope.launch {
            noteRepository.deleteNote(deletedNote.id)
        }
    }

    fun initialize() {
        noteRepository.getNotes().onEach { notesData ->
            val currentDraftId = draftState.processedNoteId.value
            notesState.notes.value = notesData.notes

            if (draftState.inProcess.value
                && currentDraftId != null
                && !notesData.notes.any { it.id == currentDraftId }) {
                draftState.reset()
            }
            notesState.viewedNote.value?.let {
                if (!notesData.notes.contains(it)) {
                    notesState.goBack()
                }
            }

            val viewedArchivedNoteFound = archivedNotesState.viewedNote.value?.let { notesData.archivedNotes.contains(it) } == true
            archivedNotesState.notes.value = notesData.archivedNotes
            if (!viewedArchivedNoteFound) {
                archivedNotesState.viewedNote.value = null
            }
        }.launchIn(coroutineScope)

        flow {
            delay(1.seconds)
            while (true) {
                noteRepository.runCleanup(Clock.System.now() - 7.days)
                emit(Unit)
                delay(5.minutes)
            }
        }.launchIn(coroutineScope)

        _initialized.value = true
    }

    private fun saveNoteDraft(draft: NoteDraft) {
        coroutineScope.launch {
            noteRepository.saveDraft(draft)
            notesState.activePane.value = ActivePane.LIST
            draftState.reset()
        }
    }

    fun unarchiveNote(unarchivedNote: Note) {
        coroutineScope.launch {
            noteRepository.unarchiveNote(unarchivedNote.id)
        }
    }

}