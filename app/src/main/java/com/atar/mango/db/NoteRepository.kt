package com.atar.mango.db

import androidx.lifecycle.LiveData

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class NoteRepository(private val noteDao: NoteDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allNotes: LiveData<List<Note>> = noteDao.loadAllNotes()


    // The suspend modifier tells the compiler that this must be called from a
    // coroutine or another suspend function.
    suspend fun insert(note: Note): Int {
        return noteDao.addNote(note).toInt()
    }

    suspend fun getNote(noteId: Int): Note {
        return noteDao.getNoteById(noteId)
    }

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

}