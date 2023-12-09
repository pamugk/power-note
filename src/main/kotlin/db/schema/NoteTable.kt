package db.schema

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object NoteTable : LongIdTable("note") {
    val createdAt = timestamp("created_at").index()
    val lastUpdatedAt = timestamp("last_updated_at")
    val archivedAt = timestamp("archived_at").index().nullable()

    val header = varchar("header", 1000)
    val content = varchar("content", 20000)
    val styledContent = text("styled_content").nullable()
}