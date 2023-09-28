package mohammadreza.ghavidel.noteapp.ui.screens.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mohammadreza.ghavidel.noteapp.R
import mohammadreza.ghavidel.noteapp.database.notes.NoteEntity


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SheetContent(
    viewModel: NoteScreenViewModel = hiltViewModel(),
    bottomSheetState: ModalBottomSheetState,
    scaffoldState: ScaffoldState
) {
    var topicText by rememberSaveable { mutableStateOf("") }
    var isTopicError by remember { mutableStateOf(false) }
    var descriptionText by rememberSaveable { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.verticalScroll(state = scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = topicText,
            onValueChange = { topicText = it },
            label = { Text(text = stringResource(R.string.note_topic)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Create,
                    contentDescription = "topic of the note"
                )
            },
            singleLine = true,
            isError = isTopicError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 16.dp)
        )

        OutlinedTextField(
            value = descriptionText,
            onValueChange = { descriptionText = it },

            label = { Text(text = stringResource(R.string.note_description)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp)
                .heightIn(min = 150.dp, max = 250.dp),
        )

        Button(
            onClick = {

                if (topicText.isEmpty()) {
                    isTopicError = true
                    focusManager.clearFocus()
                } else {
                    scope.launch {
                        viewModel.insertNote(
                            note = NoteEntity(
                                topic = topicText,
                                description = descriptionText
                            )
                        )
                        topicText = ""
                        descriptionText = ""
                        bottomSheetState.hide()
                        focusManager.clearFocus()
                        delay(100)
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "یادداشت اضافه شد.",
                            actionLabel = "ذخیره"
                        )
                    }
                }
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.save_note),
                style = MaterialTheme.typography.button
            )
        }
    }
}