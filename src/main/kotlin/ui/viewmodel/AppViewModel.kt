package ui.viewmodel

import data.repository.NoteRepository
import entity.Note
import entity.NoteDraft
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ui.data.state.AppState
import ui.data.state.ArchivePageState
import ui.data.state.NotesPageState
import ui.navigation.ActivePane
import ui.navigation.Location

class AppViewModel(private val noteRepository: NoteRepository) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val _state = MutableStateFlow(
        AppState(
            archivePageState = ArchivePageState(
                activePane = ActivePane.LIST,
                notes = emptyList(),
                viewedNote = null
            ),
            currentLocation = Location.MAIN,
            notesPageState = NotesPageState(
                activePane = ActivePane.LIST,
                draft = null,
                notes = emptyList(),
                viewedNote = null
            )
        )
    )

    init {
        noteRepository.getNotes().onEach { notesData ->
            _state.update { oldState ->
                val creatingNote = oldState.notesPageState.draft != null && oldState.notesPageState.draft.id == null
                val draftFound = oldState.notesPageState.draft?.id?.let { draftId ->
                    notesData.notes.any { it.id == draftId }
                } == true
                val viewedArchivedNoteFound = oldState.archivePageState.viewedNote?.let { notesData.archivedNotes.contains(it) } == true
                val viewedNoteFound = oldState.notesPageState.viewedNote?.let { notesData.notes.contains(it) } == true

                oldState.copy(
                    archivePageState = oldState.archivePageState.copy(
                        activePane = if (viewedArchivedNoteFound) ActivePane.VIEW else ActivePane.LIST,
                        notes = notesData.archivedNotes,
                        viewedNote = if (viewedArchivedNoteFound) oldState.archivePageState.viewedNote else null
                    ),
                    notesPageState = oldState.notesPageState.copy(
                        activePane = if (viewedNoteFound || creatingNote) oldState.notesPageState.activePane else ActivePane.LIST,
                        draft = if (draftFound || creatingNote) oldState.notesPageState.draft else null,
                        notes = notesData.notes,
                        viewedNote = if (viewedNoteFound) oldState.notesPageState.viewedNote else null
                    )
                )
            }
        }.launchIn(coroutineScope)
    }

    val state: StateFlow<AppState>
        get() = _state

    fun archiveNote(archivedNote: Note) {
        coroutineScope.launch {
            noteRepository.archiveNote(archivedNote.id)
        }
    }

    fun deleteNote(deletedNote: Note) {
        coroutineScope.launch {
            noteRepository.deleteNote(deletedNote.id)
        }
    }

    fun editNoteDraft(editedDraft: NoteDraft) {
        _state.update { oldState ->
            oldState.copy(
                notesPageState = oldState.notesPageState.copy(
                    draft = editedDraft
                )
            )
        }
    }

    fun navigate(newLocation: Location) {
        _state.update { oldState ->
            oldState.copy(
                currentLocation = newLocation
            )
        }
    }

    fun resetChanges() {
        _state.update { oldState ->
            val resetCreation = oldState.notesPageState.draft?.id == null
            oldState.copy(
                notesPageState = oldState.notesPageState.copy(
                    activePane = if (resetCreation) ActivePane.LIST else ActivePane.VIEW,
                    draft = null,
                )
            )
        }
    }

    fun returnToList() {
        _state.update { oldState ->
            when (oldState.currentLocation) {
                Location.MAIN -> oldState.copy(
                    notesPageState = oldState.notesPageState.copy(
                        activePane = ActivePane.LIST,
                        viewedNote = null
                    )
                )
                Location.ARCHIVE -> oldState.copy(
                    archivePageState = oldState.archivePageState.copy(
                        activePane = ActivePane.LIST,
                        viewedNote = null
                    )
                )
            }
        }
    }

    fun saveNoteDraft(draft: NoteDraft) {
        coroutineScope.launch {
            noteRepository.saveDraft(draft)
            _state.update { oldState ->
                oldState.copy(
                    notesPageState = oldState.notesPageState.copy(
                        activePane = ActivePane.LIST,
                        draft = null,
                        viewedNote = null
                    )
                )
            }
        }
    }

    fun startEditing(editedNote: Note?) {
        _state.update { oldState ->
            oldState.copy(
                notesPageState = oldState.notesPageState.copy(
                    activePane = ActivePane.VIEW,
                    draft =
                        if (editedNote == null)
                            NoteDraft(header = "", content = "")
                        else NoteDraft(id = editedNote.id, header = editedNote.header, content = editedNote.content),
                    viewedNote = editedNote
                )
            )
        }
    }

    fun unarchiveNote(unarchivedNote: Note) {
        coroutineScope.launch {
            noteRepository.unarchiveNote(unarchivedNote.id)
        }
    }

    fun viewNewNoteDraft() {
        _state.update { oldState ->
            oldState.copy(
                notesPageState = oldState.notesPageState.copy(
                    activePane = ActivePane.VIEW,
                    viewedNote = null
                )
            )
        }
    }

    fun viewNote(newViewedNote: Note) {
        _state.update { oldState ->
            when(oldState.currentLocation) {
                Location.MAIN -> oldState.copy(
                    notesPageState = oldState.notesPageState.copy(
                        activePane = ActivePane.VIEW,
                        viewedNote = newViewedNote
                    )
                )
                Location.ARCHIVE -> oldState.copy(
                    archivePageState = oldState.archivePageState.copy(
                        activePane = ActivePane.VIEW,
                        viewedNote = newViewedNote
                    )
                )
            }
        }
    }
}