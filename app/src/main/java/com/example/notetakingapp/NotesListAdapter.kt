package com.example.notetakingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class NotesListAdapter(var listener: OnNotesItemClickListener) : RecyclerView.Adapter<NotesListAdapter.NotesListViewHolder>() {

    inner class NotesListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesListViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.row_note_list_adapter,parent,false)
        return NotesListViewHolder(rootView);
    }

    override fun onBindViewHolder(holder: NotesListViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 5;
    }

}