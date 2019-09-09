package com.atar.mango.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Insert
    suspend fun addNote(note: Note): Long

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes_table")
    fun loadAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes_table WHERE id LIKE :id")
    suspend fun getNoteById(id: Int): Note

    @Update
    suspend fun updateNote(note: Note)
}