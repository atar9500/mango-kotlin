package com.atar.mango.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1)
abstract class MangoDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MangoDatabase? = null

        fun getDatabase(context: Context): MangoDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MangoDatabase::class.java,
                    "note_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}