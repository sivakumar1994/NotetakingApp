package com.example.notetakingapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notetakingapp.utils.AppUtils
import kotlinx.android.synthetic.main.row_note_list_adapter.view.*

class NotesListAdapter(var listener: OnNotesItemClickListener) : RecyclerView.Adapter<NotesListAdapter.NotesListViewHolder>() {
    var noteDetailList: ArrayList<NoteDetail>?  = null

    inner class NotesListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesListViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.row_note_list_adapter,parent,false)
        return NotesListViewHolder(rootView);
    }

    override fun onBindViewHolder(holder: NotesListViewHolder, position: Int) {
        holder.itemView.apply {
            noteDetailList?.get(position)?.apply {
                tv_title.text=title
                tv_notes_description.text = content
                tv_time_stamp.text=AppUtils.getTimeStamp(timeStamp)
            }
        }
        holder.itemView.setOnClickListener {
            noteDetailList?.get(position)?.let { it1 -> listener.onSelectedNotesItem(it1) }
        }
    }

    override fun getItemCount(): Int {
        return noteDetailList?.size ?: 0
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(noteDetailListUpdated: ArrayList<NoteDetail>) {
        noteDetailList = noteDetailListUpdated
        notifyDataSetChanged()
    }
}