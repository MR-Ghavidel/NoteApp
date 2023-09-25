package mohammadreza.ghavidel.noteapp.core

import androidx.room.Database
import androidx.room.RoomDatabase
import mohammadreza.ghavidel.noteapp.database.notes.NoteDao
import mohammadreza.ghavidel.noteapp.database.notes.NoteEntity

@Database(entities = [NoteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
