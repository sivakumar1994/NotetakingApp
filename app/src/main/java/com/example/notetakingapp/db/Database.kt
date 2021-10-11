package com.example.notetakingapp.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notetakingapp.db.entity.NoteDetail
import com.example.notetakingapp.db.dao.NotesDao

@androidx.room.Database(
    entities = [
        NoteDetail::class
    ], version = 1
)
abstract class Database : RoomDatabase() {
    abstract fun notesDao(): NotesDao

    companion object {
        private const val DATABASE_NAME = "notedatabase"

        @Volatile
        private var INSTANCE: Database? = null
        fun getInstance(context: Context): Database =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabse(context).also { INSTANCE = it }
            }

        private fun buildDatabse(context: Context): Database {
            val db = Room.databaseBuilder(
                context.applicationContext,
                Database::class.java,
                DATABASE_NAME
            )
            return db.build()
        }
    }
}