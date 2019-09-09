package com.atar.mango.ui.home

import android.os.Bundle
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.atar.mango.R
import com.atar.mango.db.Note
import com.atar.mango.ui.SingleEvent
import com.atar.mango.ui.events.NavigateEvent
import kotlinx.android.synthetic.main.note_fragment.*

class NoteFragment : Fragment() {

    /**
     * Data
     */
    private lateinit var mViewModel: HomeViewModel
    private var mNote = Note()
    private var mIsAddingNote = true
    private val mQueryNoteEvent = Observer<SingleEvent<Note>> {
        it.getContentIfNotHandled()?.let { note ->
            mIsAddingNote = false
            mNote = note
            fnot_title.setText(mNote.title)
            fnot_content.setText(mNote.content)
        }
    }

    /**
     * Fragment Methods
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.note_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(activity!!).get(HomeViewModel::class.java)
        mViewModel.getQueryNoteEvent().observe(this, mQueryNoteEvent)

        fnot_title.addTextChangedListener {
            mNote.title = it.toString()
        }
        fnot_content.addTextChangedListener {
            mNote.content = it.toString()
        }
    }

    override fun onDestroy() {
        mViewModel.getQueryNoteEvent().removeObserver(mQueryNoteEvent)
        if (mIsAddingNote && (mNote.title.isNotEmpty() || mNote.content.isNotEmpty())) {
            mViewModel.addNote(mNote)
        } else {
            mViewModel.editNote(mNote)
        }
        super.onDestroy()
    }

}
