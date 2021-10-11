package com.example.notetakingapp.utils

import android.app.Application
import android.content.Context
import com.example.notetakingapp.Constants.APP_PREF
import com.example.notetakingapp.Constants.SHARED_PREF_NOTES_STORED_ORDER

class SharedPreferenceHelper(var application: Context) {

    fun saveNotesOrderPref(boolean: Boolean) {
        application.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE).edit()
            .putBoolean(SHARED_PREF_NOTES_STORED_ORDER, boolean).apply()
    }

    fun getNotesOrderPref(): Boolean {
        return application.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)
            .getBoolean(SHARED_PREF_NOTES_STORED_ORDER, false)
    }
}