package com.atar.mango.ui.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.room.util.StringUtil
import com.atar.mango.db.Note

class NoteListOrder private constructor(context: Context) {

    private var sharedPref: SharedPreferences

    companion object : SingletonModel<NoteListOrder, Context>(::NoteListOrder) {
        private const val PREF_NAME = "notes-order"
        private const val ORDER = "order"
    }

    private fun reorder(positions: List<Int>) {
        with(sharedPref.edit()) {
            val positionsString = StringUtil.joinIntoString(positions)
            putString(ORDER, positionsString)
            apply()
        }
    }

    private fun getOrder(): MutableList<Int>? {
        val positionsString = sharedPref.getString(ORDER, null) ?: return mutableListOf()
        return StringUtil.splitToIntList(positionsString)
    }

    fun addNote(id: Int) {
        val orderedPositions = getOrder() ?: mutableListOf()
        orderedPositions.add(0, id)
        reorder(orderedPositions)
    }

    fun moveNote(from: Int, to: Int) {
        val orderedPositions = getOrder() ?: mutableListOf()
        val noteToMove = orderedPositions[from]
        orderedPositions.removeAt(from)
        orderedPositions.add(to, noteToMove)
        reorder(orderedPositions)
    }

    fun removeNote(index: Int) {
        val orderedPositions = getOrder() ?: mutableListOf()
        orderedPositions.removeAt(index)
        reorder(orderedPositions)
    }

    fun sortNoteList(notes: List<Note>): List<Note> {
        val orderList = getOrder() ?: return notes
        val sortedList = mutableListOf<Note>()
        for (id in orderList) {
            for (note in notes) {
                if (note.id == id) {
                    sortedList.add(note)
                    break
                }
            }
        }
        return sortedList
    }

    init {
        sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

}