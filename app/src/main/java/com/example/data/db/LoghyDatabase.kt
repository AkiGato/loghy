package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.BrainstormIdea
import com.example.data.model.FeedbackReport
import com.example.data.model.LateralSession
import com.example.data.model.NoteVersionSnapshot
import com.example.data.model.ScheduleTask
import com.example.data.model.SocraticMessage
import com.example.data.model.SwotAnalysis
import com.example.data.model.UserProfile
import com.example.data.model.VaultNote

@Database(
    entities = [
        UserProfile::class,
        BrainstormIdea::class,
        SocraticMessage::class,
        SwotAnalysis::class,
        VaultNote::class,
        NoteVersionSnapshot::class,
        LateralSession::class,
        ScheduleTask::class,
        FeedbackReport::class
    ],
    version = 7,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LoghyDatabase : RoomDatabase() {
    abstract fun loghyDao(): LoghyDao

    companion object {
        @Volatile
        private var INSTANCE: LoghyDatabase? = null

        fun getDatabase(context: Context): LoghyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LoghyDatabase::class.java,
                    "loghy_vault.db"
                ).fallbackToDestructiveMigration(dropAllTables = true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
