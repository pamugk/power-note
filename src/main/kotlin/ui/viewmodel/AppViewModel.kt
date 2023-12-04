package ui.viewmodel

import entity.Note
import entity.NoteDraft
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import ui.data.state.AppState
import ui.data.state.ArchivePageState
import ui.data.state.NotesPageState
import ui.navigation.ActivePane
import ui.navigation.Location
import java.util.concurrent.atomic.AtomicLong

class AppViewModel {
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

    private var lastUsedId = AtomicLong(0L)

    val state: StateFlow<AppState>
        get() = _state

    fun archiveNote(archivedNote: Note) {
        _state.update { oldState ->
            oldState.copy(
                archivePageState = oldState.archivePageState.copy(
                    notes = (oldState.archivePageState.notes + archivedNote.copy(archivedAt = Clock.System.now())).sortedByDescending { it.createdAt }
                ),
                notesPageState = oldState.notesPageState.copy(
                    activePane = ActivePane.LIST,
                    draft = null,
                    notes = oldState.notesPageState.notes.filterNot { it.id == archivedNote.id },
                    viewedNote = null
                )
            )
        }
    }

    fun deleteNote(deletedNote: Note) {
        _state.update { oldState ->
            oldState.copy(
                archivePageState = oldState.archivePageState.copy(
                    activePane = ActivePane.LIST,
                    notes = oldState.archivePageState.notes.filterNot { it.id == deletedNote.id },
                    viewedNote = null
                )
            )
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
        _state.update { oldState ->
            if (draft.id == null) {
                val createdNote = Note(
                    id = lastUsedId.getAndIncrement(),
                    createdAt = Clock.System.now(),
                    header = draft.header,
                    content = draft.content
                )
                oldState.copy(
                    notesPageState = oldState.notesPageState.copy(
                        draft = null,
                        notes = (oldState.notesPageState.notes + createdNote).sortedByDescending { it.createdAt },
                        viewedNote = createdNote
                    )
                )
            } else {
                oldState.notesPageState.notes.find { it.id == draft.id }?.let { oldEditedNote ->
                    val newEditedNote = oldEditedNote.copy(
                        lastUpdatedAt = Clock.System.now(),
                        header = draft.header,
                        content = draft.content
                    )

                    oldState.copy(
                        notesPageState = oldState.notesPageState.copy(
                            draft = null,
                            notes = (oldState.notesPageState.notes - oldEditedNote + newEditedNote).sortedByDescending { it.createdAt },
                            viewedNote = newEditedNote
                        )
                    )
                } ?: oldState
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
        _state.update { oldState ->
            if (oldState.archivePageState.notes.any { it.id == unarchivedNote.id }) {
                val viewingUnarchived = oldState.archivePageState.viewedNote?.id == unarchivedNote.id
                oldState.copy(
                    archivePageState = oldState.archivePageState.copy(
                        activePane = if (viewingUnarchived) ActivePane.LIST else oldState.archivePageState.activePane,
                        notes = oldState.archivePageState.notes.filterNot { it.id == unarchivedNote.id },
                        viewedNote =
                            if (viewingUnarchived) null
                            else oldState.archivePageState.viewedNote
                    ),
                    notesPageState = oldState.notesPageState.copy(
                        notes = (oldState.notesPageState.notes + unarchivedNote.copy(archivedAt = null)).sortedByDescending { it.createdAt }
                    )
                )
            }
            else oldState
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