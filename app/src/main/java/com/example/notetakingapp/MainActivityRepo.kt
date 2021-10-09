package com.example.notetakingapp

import android.app.Application

class MainActivityRepo(private val application: Application,
                       private var webEndPoint: WebEndPoint) {
  var notesDao : NotesDao = Database.getInstance(application.applicationContext).notesDao()
    suspend fun getSome() {

    }
}