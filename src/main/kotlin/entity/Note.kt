package entity

import kotlinx.datetime.Instant

data class Note(
    // Технические данные
    val id: Long,
    val createdAt: Instant,
    val archivedAt: Instant?,
    val pendingDeletion: Boolean,
    // Содержимое
    val header: String,
    val content: String,
)

data class NoteData(
    val header: String,
    val content: String,
)
