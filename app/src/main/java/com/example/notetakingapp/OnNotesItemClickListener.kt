package com.example.notetakingapp

interface OnNotesItemClickListener {
    fun onSelectedNotesItem(noteDetail: NoteDetail)
    fun onLongPressedNotesItem(noteDetail: NoteDetail?)
}