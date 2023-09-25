package mohammadreza.ghavidel.noteapp.ui.screens.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mohammadreza.ghavidel.noteapp.database.notes.NoteDao
import mohammadreza.ghavidel.noteapp.database.notes.NoteEntity
import javax.inject.Inject

@HiltViewModel
class NoteScreenViewModel @Inject constructor(
    private val noteDao: NoteDao
) : ViewModel() {

    private val _notes = MutableStateFlow<List<NoteEntity>>(emptyList())
    val notes: StateFlow<List<NoteEntity>> = _notes.asStateFlow()

    private val _bookmarkNotes = MutableStateFlow<List<NoteEntity>>(emptyList())
    val bookmarkNotes: StateFlow<List<NoteEntity>> = _bookmarkNotes.asStateFlow()

    init {
        observeNotes()
        observeBookmarkNotes()
    }

    private fun observeNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.getAllNotes().collect(_notes)
        }
    }

    private fun observeBookmarkNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.getBookmarkNotes().collect(_bookmarkNotes)
        }
    }

    fun doBookmark(note: NoteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.insertNote(note.copy(isBookmarked = !note.isBookmarked))
        }
    }

    fun updateTopic(topic: String, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val newNote = _notes.value.find {
                it.id == id
            }?.copy(topic = topic)
            noteDao.updateNote(newNote!!)
        }
    }

    fun updateDescription(description: String, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val newNote = _notes.value.find {
                it.id == id
            }?.copy(description = description)
            noteDao.updateNote(newNote!!)
        }
    }

    fun updateNote(topic: String, description: String, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val newNote = _notes.value.find {
                it.id == id
            }?.copy(topic = topic, description = description)
            noteDao.updateNote(newNote!!)
        }
    }


    fun insertNote(note: NoteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.insertNote(note)
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.deleteAllNotes()
        }
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.deleteNote(note)
        }
    }

}