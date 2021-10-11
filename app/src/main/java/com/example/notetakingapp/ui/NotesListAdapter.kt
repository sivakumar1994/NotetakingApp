package com.example.notetakingapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.notetakingapp.R
import com.example.notetakingapp.db.entity.NoteDetail
import com.example.notetakingapp.listener.OnNotesItemClickListener
import com.example.notetakingapp.utils.AppUtils
import kotlinx.android.synthetic.main.row_note_list_adapter.view.*


class NotesListAdapter(private var listener: OnNotesItemClickListener) :
    RecyclerView.Adapter<NotesListAdapter.NotesListViewHolder>() {
    private var noteDetailList: ArrayList<NoteDetail>? = null
    var context: Context? = null
    private var selectedPos = -1

    inner class NotesListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesListViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_note_list_adapter, parent, false)
        this.context = parent.context
        return NotesListViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: NotesListViewHolder, position: Int) {
        holder.itemView.apply {
            noteDetailList?.get(position)?.apply {
                tv_title.text = title
                tv_notes_description.text = content
                tv_time_stamp.text = AppUtils.getTimeStamp(timeStamp)
            }
            constraint_layout.background =
                ContextCompat.getDrawable(context, R.drawable.border_out_line)
        }

        holder.itemView.setOnClickListener {
            if (selectedPos == -1)
                noteDetailList?.get(position)?.let { it1 -> listener.onSelectedNotesItem(it1) }
        }

        holder.itemView.setOnLongClickListener {
            holder.itemView.apply {
                if (noteDetailList?.get(position)?.isPinned == false) {

                    if (selectedPos == -1) {
                        constraint_layout.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.cardview_dark_background
                            )
                        )
                    } else {
                        constraint_layout.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.cardview_dark_background
                            )
                        )
                    }
                    notifyItemChanged(selectedPos)
                    if (selectedPos == position) {
                        selectedPos = -1
                    } else {
                        selectedPos = holder.adapterPosition
                    }
                    if (selectedPos != -1) {
                        noteDetailList?.get(selectedPos)
                            ?.let { it1 -> listener.onLongPressedNotesItem(it1) }
                    } else {
                        listener.onLongPressedNotesItem(null)
                    }
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.pin_note_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return noteDetailList?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(noteDetailListUpdated: ArrayList<NoteDetail>) {
        noteDetailList = noteDetailListUpdated
        selectedPos = -1
        notifyDataSetChanged()
    }
}