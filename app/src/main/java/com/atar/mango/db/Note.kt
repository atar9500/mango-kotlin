package com.atar.mango.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String = "",
    var content: String = "",
    var color: Int = 1
)