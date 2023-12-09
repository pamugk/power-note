package entity

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days

data class Note(
    // Технические данные
    val id: Long,
    val createdAt: Instant,
    val lastUpdatedAt: Instant = createdAt,
    val archivedAt: Instant? = null,
    // Содержимое
    val header: String,
    val content: String,
    val styledContent: String?
) {
    val archived: Boolean
        get() = archivedAt != null

}

data class NoteDraft(
    val id: Long? = null,
    val header: String,
    val content: String,
    val styledContent: String? = null
)
