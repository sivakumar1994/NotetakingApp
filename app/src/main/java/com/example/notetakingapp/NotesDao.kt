package com.example.notetakingapp

import androidx.room.Dao

@Dao
abstract class NotesDao :BaseDao<NoteDetail> {
}