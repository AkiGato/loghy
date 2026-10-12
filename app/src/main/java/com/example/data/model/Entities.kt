package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * User Profile & Local Authentication configuration.
 * Stores salted PBKDF2 hash of the 4-digit PIN.
 */
@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val thinkerName: String = "Thinker",
    val pinSalt: String = "",
    val pinHash: String = "",
    val isPinSet: Boolean = false,
    val isBiometricsEnabled: Boolean = false,
    val defaultDomain: String = "Software / SaaS",
    val isTtsEnabled: Boolean = true,
    val customGeminiApiKey: String = "",
    val personalityStyle: String = "Cynical Best Friend",
    val cynicismLevel: Float = 0.75f,
    val nerdinessLevel: Float = 0.80f,
    val createdAt: Long = System.currentTimeMillis(),
    val lastFeedbackPromptTimestamp: Long = 0L,
    val appFirstLaunchTimestamp: Long = System.currentTimeMillis()
)

/**
 * An Idea or Brainstorming Session topic.
 */
@Entity(tableName = "brainstorm_ideas")
data class BrainstormIdea(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val rawConcept: String,
    val domain: String = "Software / SaaS",
    val status: String = "Active", // Active, Validated, Pivoting, Archived
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * Socratic Dialogue stream messages.
 */
@Entity(
    tableName = "socratic_messages",
    foreignKeys = [
        ForeignKey(
            entity = BrainstormIdea::class,
            parentColumns = ["id"],
            childColumns = ["ideaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("ideaId")]
)
data class SocraticMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ideaId: Long,
    val sender: String, // "USER" or "LOGHY"
    val content: String,
    val imageUri: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isKeyTakeaway: Boolean = false,
    val swotAngle: String = "NONE" // NONE, STRENGTH, WEAKNESS, THREAT, OPPORTUNITY, SOCRATIC, LATERAL
)

/**
 * 4-Angle Socratic SWOT Reality Checking Breakdown.
 * Uses plain-language headers:
 * - What Holds Up (Strengths)
 * - Vulnerable Spots (Weaknesses)
 * - Outside Hurdles (Threats)
 * - Hidden Upside (Opportunities)
 */
@Entity(
    tableName = "swot_analyses",
    foreignKeys = [
        ForeignKey(
            entity = BrainstormIdea::class,
            parentColumns = ["id"],
            childColumns = ["ideaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("ideaId")]
)
data class SwotAnalysis(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ideaId: Long,
    val domain: String,
    val whatHoldsUp: List<String>,
    val vulnerableSpots: List<String>,
    val outsideHurdles: List<String>,
    val hiddenUpside: List<String>,
    val actionableFixes: List<String>,
    val probingQuestions: List<String>,
    val readingSources: List<String>,
    val ethicsAndNeuroSustainability: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Encrypted/Private Notes Vault.
 * Supports tactile paper palette color tones (0..6).
 */
@Entity(tableName = "vault_notes")
data class VaultNote(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val paperTone: Int = 0, // 0..6
    val tags: List<String> = emptyList(),
    val isPinned: Boolean = false,
    val sourceIdeaId: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * 20-Version History Snapshot for Rollback.
 */
@Entity(
    tableName = "note_version_snapshots",
    foreignKeys = [
        ForeignKey(
            entity = VaultNote::class,
            parentColumns = ["id"],
            childColumns = ["noteId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("noteId")]
)
data class NoteVersionSnapshot(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val noteId: Long,
    val versionNumber: Int, // 1..20
    val title: String,
    val content: String,
    val snapshotTimestamp: Long = System.currentTimeMillis()
)

/**
 * Lateral Thinking Session (Edward de Bono methods).
 */
@Entity(
    tableName = "lateral_sessions",
    foreignKeys = [
        ForeignKey(
            entity = BrainstormIdea::class,
            parentColumns = ["id"],
            childColumns = ["ideaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("ideaId")]
)
data class LateralSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ideaId: Long,
    val technique: String, // RANDOM_WORD, PROVOCATION_PO, REVERSAL, SIX_HATS
    val stimulus: String,
    val userReflection: String = "",
    val loghyInsight: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Programming Schedule & Development Task for Idea Implementation.
 * Computes dev time, cognitive load pacing, and effort breakdown.
 */
@Entity(
    tableName = "schedule_tasks",
    foreignKeys = [
        ForeignKey(
            entity = BrainstormIdea::class,
            parentColumns = ["id"],
            childColumns = ["ideaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("ideaId")]
)
data class ScheduleTask(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ideaId: Long,
    val phase: String, // Architecture & Data, Core Logic, Tactile UI, Testing & Verification, Release
    val title: String,
    val description: String,
    val estimatedHours: Float,
    val effortLevel: String = "Medium", // Low, Medium, High, Critical
    val cognitiveLoad: String = "Balanced Focus", // Deep Work (Morning), Flow State, Light Administrative
    val recommendedSprint: String = "Sprint 1",
    val isCompleted: Boolean = false,
    val orderIndex: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val reminderTimestamp: Long? = null
)

/**
 * Bug reports, comments, and ideas written by users in chat or via modal.
 *
 * These never leave the device on their own. There is no uploader and no endpoint:
 * rows live here until the user taps Share in the report dialog and picks an app to
 * send them with. Keep it that way unless a privacy policy and a Play Data Safety
 * declaration are added to match.
 */
@Entity(tableName = "feedback_reports")
data class FeedbackReport(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val reportType: String, // "BUG", "COMMENT", "IDEA"
    val content: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isBatchedForMonthlyDelivery: Boolean = true,
    val status: String = "QUEUED_MONTHLY" // "QUEUED_MONTHLY", "SENT"
)

