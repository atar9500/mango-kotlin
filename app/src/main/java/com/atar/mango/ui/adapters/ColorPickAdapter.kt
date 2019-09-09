package com.atar.mango.ui.adapters

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.atar.mango.R
import com.atar.mango.db.NoteColor

class ColorPickAdapter(context: Context, colorPickListener: ColorPickListener) :
    RecyclerView.Adapter<ColorPickAdapter.ColorPickHolder>() {

    /**
     * Classes
     */
    inner class ColorPickHolder(itemView: View, colorPickListener: ColorPickListener) :
        RecyclerView.ViewHolder(itemView) {

        var mColor: ImageView = itemView.findViewById(R.id.not_color_pick)
        var mNoteColor: NoteColor = NoteColor.DEFAULT

        init {
            mColor.setOnClickListener {
                colorPickListener.onColorPick(mNoteColor)
            }
        }
    }

    interface ColorPickListener {
        fun onColorPick(noteColor: NoteColor)
    }

    /**
     * Data
     */
    private val mContext: Context = context
    private val mColorPickListener: ColorPickListener = colorPickListener
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mColors: Array<NoteColor> = NoteColor.values()

    /**
     * RecyclerView.Adapter Methods
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorPickHolder {
        val itemView = mInflater.inflate(R.layout.note_color_pick, parent, false)
        return ColorPickHolder(itemView, mColorPickListener)
    }

    override fun getItemCount(): Int {
        return mColors.size
    }

    override fun onBindViewHolder(holder: ColorPickHolder, position: Int) {
        val noteColor = mColors[position]
        holder.mNoteColor = noteColor
        val formattedColor = ContextCompat.getColor(mContext, noteColor.value)
        holder.mColor.setImageDrawable(ColorDrawable(formattedColor))
    }

}