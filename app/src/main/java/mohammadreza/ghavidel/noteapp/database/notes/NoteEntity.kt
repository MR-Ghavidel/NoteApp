package mohammadreza.ghavidel.noteapp.database.notes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val topic: String,
    val description: String,
    val isBookmarked: Boolean = false
)
