package mohammadreza.ghavidel.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import mohammadreza.ghavidel.noteapp.ui.screens.notes.NotesScreen
import mohammadreza.ghavidel.noteapp.ui.theme.NoteAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppTheme {
                NotesScreen()
            }
        }
    }
}
