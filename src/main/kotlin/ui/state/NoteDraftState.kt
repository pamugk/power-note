package ui.state

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.mohamedrejeb.richeditor.model.RichTextState
import entity.Note
import entity.NoteDraft

class NoteDraftState(
    initialState: NoteDraft = NoteDraft(content = "", header = ""),
    val maxContentLength: Int = 20000,
    val maxHeaderLength: Int = 1000,
    private val onReset: () -> Unit = {},
    private val onSave: (NoteDraft) -> Unit = {},
    private val onStartEditing: () -> Unit = {}
) {
    private val _inProcess = mutableStateOf(false)

    val content = RichTextState()
    val header = mutableStateOf(initialState.header)
    val inProcess: State<Boolean>
        get() = _inProcess
    val invalidContent: Boolean
        get() = header.value.length > maxHeaderLength
    val invalidHeader: Boolean
        get() = header.value.length > maxContentLength

    val processedNoteId = mutableStateOf<Long?>(null)

    init {
        content.setText(initialState.content)
    }

    fun editingNote(note: Note?) = inProcess.value && processedNoteId.value == note?.id

    fun startEditing(note: Note?) {
        note?.let {
            processedNoteId.value = it.id
            content.setText(it.content)
            header.value = it.header
        }
        _inProcess.value = true
        onStartEditing()
    }

    fun reset() {
        _inProcess.value = false
        content.clear()
        header.value = ""
        processedNoteId.value = null
        onReset()
    }

    fun save() =
        onSave(
            NoteDraft(
                id = processedNoteId.value,
                header = header.value, content = content.annotatedString.text
            )
        )

    fun valid() =
        content.annotatedString.isNotEmpty() && content.annotatedString.length <= maxContentLength
                && header.value.isNotEmpty() && header.value.length <= maxHeaderLength
}