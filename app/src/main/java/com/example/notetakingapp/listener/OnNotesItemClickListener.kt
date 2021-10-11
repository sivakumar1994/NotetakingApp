package com.example.notetakingapp.listener

import com.example.notetakingapp.db.entity.NoteDetail

interface OnNotesItemClickListener {
    fun onSelectedNotesItem(noteDetail: NoteDetail)
    fun onLongPressedNotesItem(noteDetail: NoteDetail?)
}