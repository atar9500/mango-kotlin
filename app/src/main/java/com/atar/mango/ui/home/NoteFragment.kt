package com.atar.mango.ui.home

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.atar.mango.R
import com.atar.mango.db.Note
import com.atar.mango.db.NoteColor
import com.atar.mango.ui.GridDivider
import com.atar.mango.ui.SingleEvent
import com.atar.mango.ui.adapters.ColorPickAdapter
import kotlinx.android.synthetic.main.note_fragment.*

class NoteFragment : Fragment() {

    companion object {
        private const val COLOR_ANIMATE_DURATION = 200L
    }

    /**
     * Data
     */
    private lateinit var mViewModel: HomeViewModel
    private var mNote = Note()
    private var mIsAddingNote = true
    private val mQueryNoteEvent = Observer<SingleEvent<Note>> {
        it.getContentIfNotHandled()?.let { note ->
            changeColor(Note.getNoteColor(note))
            fnot_title.setText(note.title)
            fnot_content.setText(note.content)
            mNote = note
            mIsAddingNote = false
        }
    }
    private val mColorPickListener = object : ColorPickAdapter.ColorPickListener {
        override fun onColorPick(noteColor: NoteColor) {
            changeColor(noteColor, true)
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

        changeColor(Note.getNoteColor(mNote))
        fnot_title.addTextChangedListener {
            mNote.title = it.toString()
        }
        fnot_content.addTextChangedListener {
            mNote.content = it.toString()
        }
        fnot_color_picker.adapter = ColorPickAdapter(context!!, mColorPickListener)
        fnot_color_picker.layoutManager =
            LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)

        val gridSpacing = resources.getDimensionPixelOffset(R.dimen.grid_spacing)
        fnot_color_picker.addItemDecoration(GridDivider(gridSpacing, 1, true))
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

    /**
     * NoteFragment
     */
    private fun changeColor(color: NoteColor, animate: Boolean = false) {
        if (animate) {
            val currentColor = Note.getNoteColor(mNote).value
            val objectAnimator = ObjectAnimator.ofObject(
                fnot_layout,
                "backgroundColor",
                ArgbEvaluator(),
                ContextCompat.getColor(context!!, currentColor),
                ContextCompat.getColor(context!!, color.value)
            )
            objectAnimator.duration = COLOR_ANIMATE_DURATION
            objectAnimator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                    mNote.color = color.id
                }

                override fun onAnimationCancel(animation: Animator?) {}

                override fun onAnimationStart(animation: Animator?) {}

            })
            objectAnimator.start()
        } else {
            fnot_layout.setBackgroundColor(ContextCompat.getColor(context!!, color.value))
            mNote.color = color.id
        }
    }

}
