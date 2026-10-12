package com.example.data.repository

import com.example.data.db.LoghyDao
import com.example.data.model.BrainstormIdea
import com.example.data.model.FeedbackReport
import com.example.data.model.LateralSession
import com.example.data.model.NoteVersionSnapshot
import com.example.data.model.SocraticMessage
import com.example.data.model.SwotAnalysis
import com.example.data.model.UserProfile
import com.example.data.model.VaultNote
import com.example.security.AuthSecurity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LoghyRepository(private val dao: LoghyDao) {

    val userProfile: Flow<UserProfile?> = dao.getUserProfile()
    val allIdeas: Flow<List<BrainstormIdea>> = dao.getAllIdeas()
    val allNotes: Flow<List<VaultNote>> = dao.getAllNotes()
    val allFeedbackReports: Flow<List<FeedbackReport>> = dao.getAllFeedbackReports()

    suspend fun getInitialProfile(): UserProfile = withContext(Dispatchers.IO) {
        val existing = dao.getUserProfileOnce()
        if (existing != null) {
            existing
        } else {
            val initial = UserProfile()
            dao.saveUserProfile(initial)
            initial
        }
    }

    suspend fun updateProfile(profile: UserProfile) = withContext(Dispatchers.IO) {
        dao.saveUserProfile(profile)
    }

    suspend fun setPin(newPin: String) = withContext(Dispatchers.IO) {
        val salt = AuthSecurity.generateSalt()
        val hash = AuthSecurity.hashPin(newPin, salt)
        val profile = dao.getUserProfileOnce() ?: UserProfile()
        dao.saveUserProfile(
            profile.copy(
                pinSalt = salt,
                pinHash = hash,
                isPinSet = true
            )
        )
    }

    suspend fun removePin() = withContext(Dispatchers.IO) {
        val profile = dao.getUserProfileOnce() ?: UserProfile()
        dao.saveUserProfile(
            profile.copy(
                pinSalt = "",
                pinHash = "",
                isPinSet = false
            )
        )
    }

    suspend fun verifyPin(pin: String): Boolean = withContext(Dispatchers.IO) {
        val profile = dao.getUserProfileOnce() ?: return@withContext false
        if (!profile.isPinSet) return@withContext true
        AuthSecurity.verifyPin(pin, profile.pinSalt, profile.pinHash)
    }

    // --- Ideas & Conversations ---
    suspend fun createIdea(title: String, rawConcept: String, domain: String): Long = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        val idea = BrainstormIdea(
            title = title.ifBlank { "Untitled Inception" },
            rawConcept = rawConcept,
            domain = domain,
            status = "Active",
            createdAt = now,
            updatedAt = now
        )
        dao.insertIdea(idea)
    }

    suspend fun updateIdea(idea: BrainstormIdea) = withContext(Dispatchers.IO) {
        dao.updateIdea(idea.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun deleteIdea(id: Long) = withContext(Dispatchers.IO) {
        dao.deleteIdea(id)
    }

    fun getMessages(ideaId: Long): Flow<List<SocraticMessage>> = dao.getMessagesForIdea(ideaId)

    suspend fun addMessage(
        ideaId: Long,
        sender: String,
        content: String,
        imageUri: String? = null,
        swotAngle: String = "NONE",
        isKeyTakeaway: Boolean = false
    ): Long = withContext(Dispatchers.IO) {
        dao.insertMessage(
            SocraticMessage(
                ideaId = ideaId,
                sender = sender,
                content = content,
                imageUri = imageUri,
                swotAngle = swotAngle,
                isKeyTakeaway = isKeyTakeaway,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun toggleTakeaway(messageId: Long, isTakeaway: Boolean) = withContext(Dispatchers.IO) {
        dao.setKeyTakeaway(messageId, isTakeaway)
    }

    suspend fun updateLegacyWelcomeMessage(newContent: String) = withContext(Dispatchers.IO) {
        dao.updateLegacyWelcomeMessage(newContent)
    }

    suspend fun syncWelcomeMessages(shortenedWelcome: String, architectureMessage: String) = withContext(Dispatchers.IO) {
        dao.updateLegacyWelcomeMessage(shortenedWelcome)
        val ideas = dao.getAllIdeasOnce()
        if (ideas.isNotEmpty()) {
            val firstIdeaId = ideas.first().id
            val messages = dao.getMessagesForIdeaOnce(firstIdeaId)
            val hasArchMessage = messages.any {
                it.content.contains("offline-first, zero-cost engine") ||
                it.content.contains("Android Keystore")
            }
            if (!hasArchMessage) {
                dao.insertMessage(
                    com.example.data.model.SocraticMessage(
                        ideaId = firstIdeaId,
                        sender = "LOGHY",
                        content = architectureMessage,
                        isKeyTakeaway = false,
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    // --- Socratic SWOT ---
    fun getLatestSwot(ideaId: Long): Flow<SwotAnalysis?> = dao.getLatestSwotForIdea(ideaId)

    suspend fun saveSwot(swot: SwotAnalysis): Long = withContext(Dispatchers.IO) {
        dao.insertSwot(swot)
    }

    // --- Vault Notes & 20-Version History ---
    fun getNote(noteId: Long): Flow<VaultNote?> = dao.getNoteById(noteId)

    fun getNoteSnapshots(noteId: Long): Flow<List<NoteVersionSnapshot>> = dao.getSnapshotsForNote(noteId)

    suspend fun createNote(
        title: String,
        content: String,
        paperTone: Int = 0,
        tags: List<String> = emptyList(),
        sourceIdeaId: Long? = null
    ): Long = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        val note = VaultNote(
            title = title.ifBlank { "Untitled Reflection" },
            content = content,
            paperTone = paperTone,
            tags = tags,
            isPinned = false,
            sourceIdeaId = sourceIdeaId,
            createdAt = now,
            updatedAt = now
        )
        val noteId = dao.insertNote(note)
        // Record Initial Version 1 Snapshot
        dao.insertSnapshot(
            NoteVersionSnapshot(
                noteId = noteId,
                versionNumber = 1,
                title = note.title,
                content = note.content,
                snapshotTimestamp = now
            )
        )
        noteId
    }

    suspend fun ensureInitialSnapshot(note: VaultNote) = withContext(Dispatchers.IO) {
        val count = dao.getSnapshotCount(note.id)
        if (count == 0) {
            dao.insertSnapshot(
                NoteVersionSnapshot(
                    noteId = note.id,
                    versionNumber = 1,
                    title = note.title,
                    content = note.content,
                    snapshotTimestamp = note.createdAt
                )
            )
        }
    }

    suspend fun updateNoteWithSnapshot(
        noteId: Long,
        newTitle: String,
        newContent: String,
        paperTone: Int? = null,
        tags: List<String>? = null,
        isPinned: Boolean? = null
    ) = withContext(Dispatchers.IO) {
        val existing = dao.getNoteByIdOnce(noteId) ?: return@withContext
        val now = System.currentTimeMillis()

        // If no prior snapshot exists for this note, capture original state as Version 1
        val initialCount = dao.getSnapshotCount(noteId)
        if (initialCount == 0) {
            dao.insertSnapshot(
                NoteVersionSnapshot(
                    noteId = noteId,
                    versionNumber = 1,
                    title = existing.title,
                    content = existing.content,
                    snapshotTimestamp = existing.createdAt
                )
            )
        }

        val updatedNote = existing.copy(
            title = newTitle,
            content = newContent,
            paperTone = paperTone ?: existing.paperTone,
            tags = tags ?: existing.tags,
            isPinned = isPinned ?: existing.isPinned,
            updatedAt = now
        )
        dao.updateNote(updatedNote)

        // Increment version number and save snapshot (Max 20 versions)
        val currentCount = dao.getSnapshotCount(noteId)
        val nextVersion = currentCount + 1
        dao.insertSnapshot(
            NoteVersionSnapshot(
                noteId = noteId,
                versionNumber = nextVersion,
                title = newTitle,
                content = newContent,
                snapshotTimestamp = now
            )
        )
        // Prune older versions exceeding 20
        dao.pruneOldSnapshots(noteId)
    }

    suspend fun rollbackNoteToVersion(noteId: Long, snapshot: NoteVersionSnapshot) = withContext(Dispatchers.IO) {
        val existing = dao.getNoteByIdOnce(noteId) ?: return@withContext
        val now = System.currentTimeMillis()
        val rolledBack = existing.copy(
            title = snapshot.title,
            content = snapshot.content,
            updatedAt = now
        )
        dao.updateNote(rolledBack)
        val currentCount = dao.getSnapshotCount(noteId)
        dao.insertSnapshot(
            NoteVersionSnapshot(
                noteId = noteId,
                versionNumber = currentCount + 1,
                title = snapshot.title,
                content = snapshot.content,
                snapshotTimestamp = now
            )
        )
        dao.pruneOldSnapshots(noteId)
    }

    suspend fun deleteNote(noteId: Long) = withContext(Dispatchers.IO) {
        dao.deleteNote(noteId)
    }

    // --- Lateral Thinking ---
    fun getLateralSessions(ideaId: Long): Flow<List<LateralSession>> = dao.getLateralSessionsForIdea(ideaId)

    suspend fun saveLateralSession(session: LateralSession): Long = withContext(Dispatchers.IO) {
        dao.insertLateralSession(session)
    }

    // --- Schedule & Task Management ---
    fun getScheduleTasks(ideaId: Long): Flow<List<com.example.data.model.ScheduleTask>> =
        dao.getTasksForIdea(ideaId)

    suspend fun saveScheduleTasks(tasks: List<com.example.data.model.ScheduleTask>) = withContext(Dispatchers.IO) {
        dao.insertTasks(tasks)
    }

    suspend fun addTask(task: com.example.data.model.ScheduleTask): Long = withContext(Dispatchers.IO) {
        dao.insertTask(task)
    }

    suspend fun toggleTaskCompleted(taskId: Long, isCompleted: Boolean) = withContext(Dispatchers.IO) {
        dao.setTaskCompleted(taskId, isCompleted)
    }

    suspend fun setTaskReminder(taskId: Long, reminderTimestamp: Long?) = withContext(Dispatchers.IO) {
        dao.setTaskReminder(taskId, reminderTimestamp)
    }

    suspend fun deleteTask(taskId: Long) = withContext(Dispatchers.IO) {
        dao.deleteTask(taskId)
    }

    suspend fun clearTasks(ideaId: Long) = withContext(Dispatchers.IO) {
        dao.clearTasksForIdea(ideaId)
    }

    // --- Bug & Comments Reports ---
    suspend fun saveFeedbackReport(reportType: String, content: String): Long = withContext(Dispatchers.IO) {
        dao.insertFeedbackReport(
            FeedbackReport(
                reportType = reportType,
                content = content,
                createdAt = System.currentTimeMillis(),
                isBatchedForMonthlyDelivery = true,
                status = "QUEUED_MONTHLY"
            )
        )
    }

    suspend fun getFeedbackReportsOnce(): List<FeedbackReport> = withContext(Dispatchers.IO) {
        dao.getAllFeedbackReportsOnce()
    }

    suspend fun deleteFeedbackReport(id: Long) = withContext(Dispatchers.IO) {
        dao.deleteFeedbackReport(id)
    }

    suspend fun updateFeedbackReportsStatus(ids: List<Long>, status: String) = withContext(Dispatchers.IO) {
        dao.updateFeedbackReportsStatus(ids, status)
    }
}
