package data.repository

import data.state.DatasetState
import db.schema.NoteTable
import entity.Note
import entity.NoteDraft
import environment.getDataPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.sql.Connection

private fun mapNoteRow(row: ResultRow) =
    Note(
        id = row[NoteTable.id].value,
        createdAt = row[NoteTable.createdAt],
        lastUpdatedAt = row[NoteTable.lastUpdatedAt],
        archivedAt = row[NoteTable.archivedAt],
        header = row[NoteTable.header],
        content = row[NoteTable.content],
        styledContent = row[NoteTable.styledContent]
    )

class DbNoteRepository: NoteRepository {
    private val db by lazy {
        Database.connect("jdbc:sqlite:${getDataPath()}data.db", "org.sqlite.JDBC")
    }

    private val notes = MutableStateFlow(DatasetState(emptyList(), emptyList()))

    override suspend fun archiveNote(archivedNoteId: Long) {
        newSuspendedTransaction(
            Dispatchers.IO, db = db,
            transactionIsolation = Connection.TRANSACTION_SERIALIZABLE) {
            addLogger(Slf4jSqlDebugLogger)

            val operationTime = Clock.System.now()
            NoteTable.update({ NoteTable.id eq archivedNoteId }) {
                it[archivedAt] = operationTime
            }
        }
        fetchNotes()
    }

    override suspend fun deleteNote(deletedNoteId: Long) {
        newSuspendedTransaction(
            Dispatchers.IO, db = db,
            transactionIsolation = Connection.TRANSACTION_SERIALIZABLE) {
            addLogger(Slf4jSqlDebugLogger)

            NoteTable.deleteWhere { NoteTable.id eq deletedNoteId }
        }
        fetchNotes()
    }

    private suspend fun fetchNotes() {
        val notesState = newSuspendedTransaction(
            Dispatchers.IO, db = db,
            transactionIsolation = Connection.TRANSACTION_SERIALIZABLE) {
            addLogger(Slf4jSqlDebugLogger)

            val archivedNotes = NoteTable
                .select { NoteTable.archivedAt.isNotNull() }
                .orderBy(NoteTable.createdAt to SortOrder.DESC)
                .toList().map { mapNoteRow(it) }
            val notes = NoteTable
                .select { NoteTable.archivedAt.isNull() }
                .orderBy(NoteTable.createdAt to SortOrder.DESC)
                .toList().map { mapNoteRow(it) }
            DatasetState(archivedNotes, notes)
        }
        notes.value = notesState
    }

    override fun getNotes(): StateFlow<DatasetState> = notes

    suspend fun initialize() {
        newSuspendedTransaction(
            Dispatchers.IO, db = db,
            transactionIsolation = Connection.TRANSACTION_SERIALIZABLE) {
            addLogger(Slf4jSqlDebugLogger)

            SchemaUtils.createMissingTablesAndColumns(NoteTable)
        }
        fetchNotes()
    }

    override suspend fun runCleanup(threshold: Instant) {
        val deletedCount = newSuspendedTransaction(
            Dispatchers.IO, db = db,
            transactionIsolation = Connection.TRANSACTION_SERIALIZABLE) {
            addLogger(Slf4jSqlDebugLogger)

            NoteTable.deleteWhere { archivedAt.isNotNull().and( archivedAt lessEq threshold) }
        }
        if (deletedCount > 0) {
            fetchNotes()
        }
    }

    override suspend fun saveDraft(savedDraft: NoteDraft) {
        newSuspendedTransaction(
            Dispatchers.IO, db = db,
            transactionIsolation = Connection.TRANSACTION_SERIALIZABLE) {
            addLogger(Slf4jSqlDebugLogger)

            val operationTime = Clock.System.now()
            if (savedDraft.id == null) {
                NoteTable.insert {
                    it[createdAt] = operationTime
                    it[lastUpdatedAt] = operationTime
                    it[header] = savedDraft.header
                    it[content] = savedDraft.content
                    it[styledContent] = savedDraft.styledContent
                }
            } else {
                NoteTable.update({ (NoteTable.id eq savedDraft.id).and(NoteTable.archivedAt.isNull()) }) {
                    it[lastUpdatedAt] = operationTime
                    it[header] = savedDraft.header
                    it[content] = savedDraft.content
                    it[styledContent] = savedDraft.styledContent
                }
            }
        }
        fetchNotes()
    }

    override suspend fun unarchiveNote(unarchivedNoteId: Long) {
        newSuspendedTransaction(
            Dispatchers.IO, db = db,
            transactionIsolation = Connection.TRANSACTION_SERIALIZABLE) {
            addLogger(Slf4jSqlDebugLogger)

            NoteTable.update({ NoteTable.id eq unarchivedNoteId }) {
                it[archivedAt] = null
            }
        }
        fetchNotes()
    }
}