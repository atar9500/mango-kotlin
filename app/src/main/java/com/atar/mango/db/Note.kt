package com.atar.mango.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String = "",
    var content: String = "",
    var color: Int = NoteColor.DEFAULT.id
) {
    companion object {
        private val noteColors = NoteColor.values()
        fun getNoteColor(note: Note): NoteColor {
            var chosenColor = NoteColor.DEFAULT
            for (i in noteColors.indices) {
                if (noteColors[i].id == note.color) {
                    chosenColor = noteColors[i]
                    break
                }
            }
            return chosenColor
        }
    }
}