package com.atar.mango.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.atar.mango.R
import com.atar.mango.db.Note

class NotesAdapter internal constructor(
    context: Context,
    private val mNoteClickListener: NoteClickListener
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    /**
     * Data
     */
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mNotes = mutableListOf<Note>()

    /**
     * Inner Classes
     */
    inner class NoteViewHolder(itemView: View, noteClickListener: NoteClickListener) : RecyclerView.ViewHolder(itemView) {
        val mTitleView: TextView = itemView.findViewById(R.id.note_title)
        val mSnippetView: TextView = itemView.findViewById(R.id.note_snippet)
        val mCardView: CardView = itemView.findViewById(R.id.note_card)
        var mNote: Note? = null

        init {
            mCardView.setOnClickListener{
                noteClickListener.onNoteClicked(mNote)
            }
        }

    }

    interface NoteClickListener {
        fun onNoteClicked(note: Note?)
    }

    /**
     * RecyclerView.Adapter Methods
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = mInflater.inflate(R.layout.note, parent, false)
        return NoteViewHolder(itemView, mNoteClickListener)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = mNotes[position]
        holder.mNote = current
        holder.mTitleView.text = current.title
        holder.mSnippetView.text = current.content
    }

    override fun getItemCount() = mNotes.size

    /**
     * NotesAdapter Methods
     */
    internal fun updateNotes(notes: List<Note>) {
        if (mNotes.isEmpty()) {
            mNotes.addAll(notes)
            notifyDataSetChanged()
        } else if (mNotes.size + 1 == notes.size) {
            mNotes.add(0, notes[0])
            notifyItemInserted(0)
        }
    }

    internal fun moveNote(from: Int, to: Int) {
        val noteToMove = mNotes[from]
        mNotes.removeAt(from)
        mNotes.add(to, noteToMove)
        notifyItemMoved(from, to)
    }
}