package com.atar.mango.ui.home

import android.app.Application
import androidx.lifecycle.*
import com.atar.mango.db.MangoDatabase
import com.atar.mango.db.Note
import com.atar.mango.db.NoteRepository
import com.atar.mango.ui.SingleEvent
import com.atar.mango.ui.events.NavigateEvent
import com.atar.mango.ui.utils.NoteListOrder
import kotlinx.coroutines.launch

// Class extends AndroidViewModel and requires application as a parameter.
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Notes List Data
     */
    // The ViewModel maintains a reference to the mRepository to get data.
    private val mRepository: NoteRepository
    private val mNotesOrder: NoteListOrder

    // LiveData gives us updated Notes when they change.
    private val mAllNotes: LiveData<List<Note>>
    fun getAllNotes(): LiveData<List<Note>> {
        return mAllNotes
    }

    // The implementation of insert() is completely hidden from the UI.
    // We don't want insert to block the main thread, so we're launching a new
    // coroutine. ViewModels have a coroutine scope based on their lifecycle called
    // viewModelScope which we can use here.
    fun addNote(note: Note) = viewModelScope.launch {
        val id = mRepository.insert(note)
        mNotesOrder.addNote(id)
    }

    fun editNote(note: Note) = viewModelScope.launch {
        mRepository.updateNote(note)
    }

    fun moveNote(from: Int, to: Int) {
        mNotesOrder.moveNote(from, to)
    }

    /**
     * NavigateEvent
     */
    private val mNavigateEvent = MutableLiveData<SingleEvent<NavigateEvent>>()

    fun getNavigateEvent(): LiveData<SingleEvent<NavigateEvent>> {
        return mNavigateEvent
    }

    fun setNavigateEvent(event: NavigateEvent) {
        mNavigateEvent.value = SingleEvent(event)
    }

    /**
     * QueryNoteEvent
     */
    private val mQueryNoteEvent = MutableLiveData<SingleEvent<Note>>()

    fun getQueryNoteEvent(): LiveData<SingleEvent<Note>> {
        return mQueryNoteEvent
    }

    fun queryNote(noteId: Int) = viewModelScope.launch {
        val note = mRepository.getNote(noteId)
        mQueryNoteEvent.value = SingleEvent(note)
    }

    /**
     * HomeViewModel
     */
    init {
        // Gets reference to NoteDao from NoteRoomDatabase to construct
        // the correct NoteRepository.
        val notesDao = MangoDatabase.getDatabase(application).noteDao()
        mRepository = NoteRepository(notesDao)
        mAllNotes = mRepository.allNotes
        mNotesOrder = NoteListOrder.getInstance(application)
    }
}
