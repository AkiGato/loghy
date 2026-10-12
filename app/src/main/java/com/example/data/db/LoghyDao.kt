package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BrainstormIdea
import com.example.data.model.FeedbackReport
import com.example.data.model.LateralSession
import com.example.data.model.NoteVersionSnapshot
import com.example.data.model.SocraticMessage
import com.example.data.model.SwotAnalysis
import com.example.data.model.UserProfile
import com.example.data.model.VaultNote
import kotlinx.coroutines.flow.Flow

@Dao
interface LoghyDao {

    // --- User Profile & Auth ---
    @Query("SELECT * FROM user_profiles WHERE id = 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profiles WHERE id = 1")
    suspend fun getUserProfileOnce(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(profile: UserProfile)

    // --- Ideas & Brainstorms ---
    @Query("SELECT * FROM brainstorm_ideas ORDER BY updatedAt DESC")
    fun getAllIdeas(): Flow<List<BrainstormIdea>>

    @Query("SELECT * FROM brainstorm_ideas WHERE id = :id")
    fun getIdeaById(id: Long): Flow<BrainstormIdea?>

    @Query("SELECT * FROM brainstorm_ideas WHERE id = :id")
    suspend fun getIdeaByIdOnce(id: Long): BrainstormIdea?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIdea(idea: BrainstormIdea): Long

    @Update
    suspend fun updateIdea(idea: BrainstormIdea)

    @Query("DELETE FROM brainstorm_ideas WHERE id = :id")
    suspend fun deleteIdea(id: Long)

    // --- Socratic Messages ---
    @Query("SELECT * FROM socratic_messages WHERE ideaId = :ideaId ORDER BY timestamp ASC")
    fun getMessagesForIdea(ideaId: Long): Flow<List<SocraticMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: SocraticMessage): Long

    @Query("UPDATE socratic_messages SET isKeyTakeaway = :isKeyTakeaway WHERE id = :messageId")
    suspend fun setKeyTakeaway(messageId: Long, isKeyTakeaway: Boolean)

    @Query("SELECT * FROM brainstorm_ideas ORDER BY updatedAt DESC")
    suspend fun getAllIdeasOnce(): List<BrainstormIdea>

    @Query("SELECT * FROM socratic_messages WHERE ideaId = :ideaId ORDER BY timestamp ASC")
    suspend fun getMessagesForIdeaOnce(ideaId: Long): List<SocraticMessage>

    @Query("UPDATE socratic_messages SET content = :newContent WHERE content LIKE '%Your ideas are strictly safe from AI here%' OR content LIKE '%Hey there, genius! I''m Loghy%'")
    suspend fun updateLegacyWelcomeMessage(newContent: String)

    // --- Socratic SWOT Reality Checking ---
    @Query("SELECT * FROM swot_analyses WHERE ideaId = :ideaId ORDER BY createdAt DESC LIMIT 1")
    fun getLatestSwotForIdea(ideaId: Long): Flow<SwotAnalysis?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSwot(swot: SwotAnalysis): Long

    // --- Vault Notes ---
    @Query("SELECT * FROM vault_notes ORDER BY isPinned DESC, updatedAt DESC")
    fun getAllNotes(): Flow<List<VaultNote>>

    @Query("SELECT * FROM vault_notes WHERE id = :id")
    fun getNoteById(id: Long): Flow<VaultNote?>

    @Query("SELECT * FROM vault_notes WHERE id = :id")
    suspend fun getNoteByIdOnce(id: Long): VaultNote?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: VaultNote): Long

    @Update
    suspend fun updateNote(note: VaultNote)

    @Query("DELETE FROM vault_notes WHERE id = :id")
    suspend fun deleteNote(id: Long)

    // --- 20-Version Rollback Snapshots ---
    @Query("SELECT * FROM note_version_snapshots WHERE noteId = :noteId ORDER BY versionNumber DESC")
    fun getSnapshotsForNote(noteId: Long): Flow<List<NoteVersionSnapshot>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnapshot(snapshot: NoteVersionSnapshot): Long

    @Query("SELECT COUNT(*) FROM note_version_snapshots WHERE noteId = :noteId")
    suspend fun getSnapshotCount(noteId: Long): Int

    @Query("DELETE FROM note_version_snapshots WHERE noteId = :noteId AND id NOT IN (SELECT id FROM note_version_snapshots WHERE noteId = :noteId ORDER BY versionNumber DESC LIMIT 20)")
    suspend fun pruneOldSnapshots(noteId: Long)

    // --- Lateral Thinking Sessions ---
    @Query("SELECT * FROM lateral_sessions WHERE ideaId = :ideaId ORDER BY timestamp DESC")
    fun getLateralSessionsForIdea(ideaId: Long): Flow<List<LateralSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLateralSession(session: LateralSession): Long

    // --- Development Work & Programming Schedule Tasks ---
    @Query("SELECT * FROM schedule_tasks WHERE ideaId = :ideaId ORDER BY orderIndex ASC, id ASC")
    fun getTasksForIdea(ideaId: Long): Flow<List<com.example.data.model.ScheduleTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: com.example.data.model.ScheduleTask): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<com.example.data.model.ScheduleTask>)

    @Update
    suspend fun updateTask(task: com.example.data.model.ScheduleTask)

    @Query("UPDATE schedule_tasks SET isCompleted = :isCompleted WHERE id = :taskId")
    suspend fun setTaskCompleted(taskId: Long, isCompleted: Boolean)

    @Query("UPDATE schedule_tasks SET reminderTimestamp = :reminderTimestamp WHERE id = :taskId")
    suspend fun setTaskReminder(taskId: Long, reminderTimestamp: Long?)

    @Query("DELETE FROM schedule_tasks WHERE id = :taskId")
    suspend fun deleteTask(taskId: Long)

    @Query("DELETE FROM schedule_tasks WHERE ideaId = :ideaId")
    suspend fun clearTasksForIdea(ideaId: Long)

    // --- Bug & Comments Reports (Monthly Aki Sync) ---
    @Query("SELECT * FROM feedback_reports ORDER BY createdAt DESC")
    fun getAllFeedbackReports(): Flow<List<FeedbackReport>>

    @Query("SELECT * FROM feedback_reports ORDER BY createdAt DESC")
    suspend fun getAllFeedbackReportsOnce(): List<FeedbackReport>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedbackReport(report: FeedbackReport): Long

    @Query("DELETE FROM feedback_reports WHERE id = :id")
    suspend fun deleteFeedbackReport(id: Long)

    @Query("UPDATE feedback_reports SET status = :status WHERE id IN (:ids)")
    suspend fun updateFeedbackReportsStatus(ids: List<Long>, status: String)
}
