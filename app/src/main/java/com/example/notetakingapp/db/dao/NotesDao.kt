package com.example.notetakingapp.db.dao

import androidx.room.*
import com.example.notetakingapp.utils.Constants
import com.example.notetakingapp.db.entity.NoteDetail

@Dao
abstract class NotesDao : BaseDao<NoteDetail> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertNotesDetails(noteDetail: NoteDetail)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun updateNotesDetails(noteDetail: NoteDetail)

    @Query("UPDATE ${Constants.NEW_NOTE_CREATION} SET isPinned =:isPinEnable WHERE id =:id")
    abstract suspend fun updatePinStatus(id : Long, isPinEnable: Boolean)

    @Query("SELECT * FROM ${Constants.NEW_NOTE_CREATION} ORDER BY timeStamp ASC")
    abstract suspend fun fetchAllNotesDetails():List<NoteDetail>

    @Query("SELECT * FROM ${Constants.NEW_NOTE_CREATION} WHERE isPinned =:pinned ORDER BY timeStamp ASC")
    abstract suspend fun fetchPinedNotesDetails(pinned: Boolean):List<NoteDetail>

    @Query("SELECT * FROM ${Constants.NEW_NOTE_CREATION} WHERE id =:id")
    abstract suspend fun fetchSpecificNotesDetails(id: Long): NoteDetail

    @Query("DELETE FROM ${Constants.NEW_NOTE_CREATION} WHERE id=:id ")
    abstract suspend fun deleteNotesDetail(id:Long)
}