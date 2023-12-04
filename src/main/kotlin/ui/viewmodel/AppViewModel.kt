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

private data class AppUiState(
    val archivePageUiState: ArchivePageUiState,
    val currentLocation: Location,
    val notesPageUiState: NotesPageUiState
)

private data class ArchivePageUiState(
    val activePane: ActivePane,
    val lastViewedNote: Note?
)

private data class NotesPageUiState(
    val activePane: ActivePane,
    val draft: NoteDraft?,
    val lastViewedNote: Note?
)

private fun <T1, T2, R> combineState(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    scope: CoroutineScope,
    sharingStarted: SharingStarted,
    transform: (T1, T2) -> R
): StateFlow<R> =
    combine(flow1, flow2) { o1, o2 -> transform.invoke(o1, o2) }
        .stateIn(scope, sharingStarted, transform.invoke(flow1.value, flow2.value))

class AppViewModel(private val noteRepository: NoteRepository) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val _state = MutableStateFlow(
        AppUiState(
            archivePageUiState = ArchivePageUiState(
                activePane = ActivePane.LIST,
                lastViewedNote = null
            ),
            currentLocation = Location.MAIN,
            notesPageUiState = NotesPageUiState(
                activePane = ActivePane.LIST,
                draft = null,
                lastViewedNote = null
            )
        )
    )
    private val _combinedState = combineState(
        _state, noteRepository.getNotes(),
        coroutineScope, SharingStarted.Eagerly
    ) { appUiState, notesData ->
        val creatingNote = appUiState.notesPageUiState.draft != null && appUiState.notesPageUiState.draft.id == null
        val draftFound = appUiState.notesPageUiState.draft?.id?.let { draftId ->
            notesData.first.any { it.id == draftId } || notesData.second.any { it.id == draftId }
        } == true
        val viewedArchivedNoteFound = appUiState.archivePageUiState.lastViewedNote?.let { notesData.first.contains(it) } == true
        val viewedNoteFound = appUiState.notesPageUiState.lastViewedNote?.let { notesData.second.contains(it) } == true

        AppState(
            archivePageState = ArchivePageState(
                activePane = if (viewedArchivedNoteFound) ActivePane.VIEW else ActivePane.LIST,
                notes = notesData.first,
                viewedNote = if (viewedArchivedNoteFound) appUiState.archivePageUiState.lastViewedNote else null
            ),
            currentLocation = appUiState.currentLocation,
            notesPageState = NotesPageState(
                activePane = if (viewedNoteFound || creatingNote) appUiState.notesPageUiState.activePane else ActivePane.LIST,
                draft = if (draftFound || creatingNote) appUiState.notesPageUiState.draft else null,
                notes = notesData.second,
                viewedNote = if (viewedNoteFound) appUiState.notesPageUiState.lastViewedNote else null
            )
        )
    }

    val state: StateFlow<AppState>
        get() = _combinedState

    fun archiveNote(archivedNote: Note) {
        coroutineScope.launch {
            noteRepository.archiveNote(archivedNote)
            _state.update { oldState ->
                oldState.copy(
                    notesPageUiState = NotesPageUiState(
                        activePane = ActivePane.LIST,
                        draft = null,
                        lastViewedNote = null
                    )
                )
            }
        }
    }

    fun deleteNote(deletedNote: Note) {
        coroutineScope.launch {
            noteRepository.deleteNote(deletedNote)
            _state.update { oldState ->
                oldState.copy(
                    archivePageUiState = ArchivePageUiState(
                        activePane = ActivePane.LIST,
                        lastViewedNote = null
                    )
                )
            }
        }
    }

    fun editNoteDraft(editedDraft: NoteDraft) {
        _state.update { oldState ->
            oldState.copy(
                notesPageUiState = oldState.notesPageUiState.copy(
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
            val resetCreation = oldState.notesPageUiState.draft?.id == null
            oldState.copy(
                notesPageUiState = oldState.notesPageUiState.copy(
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
                    notesPageUiState = oldState.notesPageUiState.copy(
                        activePane = ActivePane.LIST,
                        lastViewedNote = null
                    )
                )
                Location.ARCHIVE -> oldState.copy(
                    archivePageUiState = oldState.archivePageUiState.copy(
                        activePane = ActivePane.LIST,
                        lastViewedNote = null
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
                    notesPageUiState = NotesPageUiState(
                        activePane = ActivePane.LIST,
                        draft = null,
                        lastViewedNote = null
                    )
                )
            }
        }
    }

    fun startEditing(editedNote: Note?) {
        _state.update { oldState ->
            oldState.copy(
                notesPageUiState = oldState.notesPageUiState.copy(
                    activePane = ActivePane.VIEW,
                    draft =
                        if (editedNote == null)
                            NoteDraft(header = "", content = "")
                        else NoteDraft(id = editedNote.id, header = editedNote.header, content = editedNote.content),
                    lastViewedNote = editedNote
                )
            )
        }
    }

    fun unarchiveNote(unarchivedNote: Note) {
        coroutineScope.launch {
            noteRepository.unarchiveNote(unarchivedNote)
        }
    }

    fun viewNewNoteDraft() {
        _state.update { oldState ->
            oldState.copy(
                notesPageUiState = oldState.notesPageUiState.copy(
                    activePane = ActivePane.VIEW,
                    lastViewedNote = null
                )
            )
        }
    }

    fun viewNote(newViewedNote: Note) {
        _state.update { oldState ->
            when(oldState.currentLocation) {
                Location.MAIN -> oldState.copy(
                    notesPageUiState = oldState.notesPageUiState.copy(
                        activePane = ActivePane.VIEW,
                        lastViewedNote = newViewedNote
                    )
                )
                Location.ARCHIVE -> oldState.copy(
                    archivePageUiState = oldState.archivePageUiState.copy(
                        activePane = ActivePane.VIEW,
                        lastViewedNote = newViewedNote
                    )
                )
            }
        }
    }
}