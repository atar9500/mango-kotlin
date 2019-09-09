package com.atar.mango.ui.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.atar.mango.db.Note
import com.atar.mango.ui.GridDivider
import com.atar.mango.ui.adapters.NotesAdapter
import com.atar.mango.ui.events.NavigateEvent
import kotlinx.android.synthetic.main.note_list_fragment.*
import androidx.recyclerview.widget.ItemTouchHelper
import com.atar.mango.ui.adapters.GridItemTouchHelper
import com.atar.mango.ui.utils.NoteListOrder


class NoteListFragment : Fragment() {

    companion object {
        const val LIST_ROWS = 2
        const val LIST_ORIENTATION = StaggeredGridLayoutManager.VERTICAL
    }

    /**
     * Data
     */
    private lateinit var mViewModel: HomeViewModel
    private lateinit var mNotesAdapter: NotesAdapter
    private val mNotesObserver = Observer<List<Note>> {
        val sortedNotes = NoteListOrder.getInstance(context!!).sortNoteList(it)
        mNotesAdapter.updateNotes(sortedNotes)
    }
    private val mNoteClickListener = object : NotesAdapter.NoteClickListener {
        override fun onNoteClicked(note: Note?) {
            if (note == null) {
                return
            }
            mViewModel.queryNote(note.id)
            mViewModel.setNavigateEvent(
                NavigateEvent(com.atar.mango.R.id.action_noteListFragment_to_noteFragment)
            )
        }
    }
    private val mGridItemTouchHelper = GridItemTouchHelper(object : GridItemTouchHelper.Callback {
        override fun onMove(from: Int, to: Int) {
            mViewModel.moveNote(from, to)
            mNotesAdapter.moveNote(from, to)
        }

        override fun onSwipe(position: Int) {

        }

        override fun enableDrag(): Boolean {
            return mNotesAdapter.itemCount > 1
        }
    })

    /**
     * Fragment Methods
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.atar.mango.R.layout.note_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(activity!!).get(HomeViewModel::class.java)
        mViewModel.getAllNotes().observe(this, mNotesObserver)

        val gridSpacing = resources.getDimensionPixelOffset(com.atar.mango.R.dimen.grid_spacing)
        fnol_list.addItemDecoration(GridDivider(gridSpacing, LIST_ROWS, false))
        mNotesAdapter = NotesAdapter(context!!, mNoteClickListener)
        fnol_list.adapter = mNotesAdapter
        fnol_list.layoutManager = StaggeredGridLayoutManager(LIST_ROWS, LIST_ORIENTATION)

        fnol_add.setOnClickListener {
            mViewModel.setNavigateEvent(NavigateEvent(com.atar.mango.R.id.action_noteListFragment_to_noteFragment))
        }


        ItemTouchHelper(mGridItemTouchHelper).attachToRecyclerView(fnol_list)

    }

    override fun onDestroyView() {
        mViewModel.getAllNotes().removeObserver(mNotesObserver)
        super.onDestroyView()
    }

}
