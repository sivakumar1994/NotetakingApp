package com.example.notetakingapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlin.coroutines.coroutineContext

class MainActivityRepo(
    private val application: Application,
    private var webEndPoint: WebEndPoint
) {
    private val notesDao: NotesDao

    init {
        notesDao = Database.getInstance(application).notesDao()
    }

    suspend fun insertNotesDetails(notesDetails: NoteDetail) {
        notesDao.insertNotesDetails(notesDetails)
    }

    suspend fun updateNotesDetails(notesDetails: NoteDetail) {
        notesDao.updateNotesDetails(notesDetails)
    }

    suspend fun getNotesDetails(liveData: MutableLiveData<List<NoteDetail>>) {
        try {
            val result = notesDao.fetchAllNotesDetails()
            liveData.postValue(result)
        } catch (e: Exception) {
            Log.d("=====>", e.toString())
        }
    }

    suspend fun getPinnedNotesDetails(
        isPinned: Boolean,
        liveData: MutableLiveData<List<NoteDetail>>
    ) {
        try {
            val result = notesDao.fetchPinedNotesDetails(isPinned)
            liveData.postValue(result)
        } catch (e: Exception) {
            Log.d("=====>", e.toString())
        }
    }

    suspend fun getSpecificNoteDataFromId(id: Long, liveData: MutableLiveData<NoteDetail>) {
        try {
            val result = notesDao.fetchSpecificNotesDetails(id)
            liveData.postValue(result)
        } catch (e: Exception) {
            Log.d("=====>", e.toString())
        }
    }

    suspend fun deleteNoteDetail(id: Long) {
        notesDao.deleteNotesDetail(id)
    }

    suspend fun updatePinStatus(
        id: Long,
        isPinEnable: Boolean,
        liveData: MutableLiveData<Long>
    ) {
        try {
             notesDao.updatePinStatus(id, isPinEnable)
            liveData.postValue(0L)
        } catch (e: Exception) {
            Log.d("=====>", e.toString())
        }
    }
}