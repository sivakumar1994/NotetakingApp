package com.example.notetakingapp

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = Constants.NEW_NOTE_CREATION)
class NoteDetail(
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "content")
    var content: String,
    @ColumnInfo(name = "imageUrl")
    var imageUrl: String,
    @ColumnInfo(name = "timeStamp")
    var timeStamp: String,
    @ColumnInfo(name = "isPinned")
    var isPinned: Boolean = false,
    @ColumnInfo(name = "isSynced")
    var isSynced: Boolean = false,
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
) : Parcelable

