package mohammadreza.ghavidel.noteapp.ui.screens.notes

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mohammadreza.ghavidel.noteapp.R
import mohammadreza.ghavidel.noteapp.database.notes.NoteEntity
import mohammadreza.ghavidel.noteapp.ui.theme.red


@Composable
fun EditOrShowDialog(
    isShowNoteDialog: Boolean,
    note: NoteEntity,
    viewModel: NoteScreenViewModel,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    clipboardManager: ClipboardManager,
    context: Context,
    action: (Boolean) -> Unit
) {
    var isShowNoteDialog1 = isShowNoteDialog
    if (isShowNoteDialog1) {
        var checkedEdit by remember { mutableStateOf(false) }
        var topicValue by remember { mutableStateOf(note.topic) }
        var descriptionValue by remember { mutableStateOf(note.description) }

        AlertDialog(
            onDismissRequest = { },
            buttons = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        enabled = (topicValue != note.topic || descriptionValue != note.description) && topicValue.isNotEmpty(),
                        onClick = {
                            isShowNoteDialog1 = false
                            action(false)
                            viewModel.updateNote(
                                topic = topicValue,
                                description = descriptionValue,
                                id = note.id
                            )
                            scope.launch(Dispatchers.IO) {
                                delay(100)
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = "ویرایش انجام شد.",
                                    actionLabel = "ویرایش"
                                )
                            }

                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp, bottom = 16.dp)
                    ) {
                        Text(
                            text = "ذخیره و بستن",
                            style = MaterialTheme.typography.button,
                            color = red
                        )
                    }
                    Button(
                        onClick = {
                            isShowNoteDialog1 = false
                            action(false)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 16.dp, bottom = 16.dp)
                    ) {
                        Text(
                            text = "بستن",
                            style = MaterialTheme.typography.button
                        )
                    }
                }
            },
            text = {
                Scaffold(
                    modifier = Modifier.heightIn(max = 300.dp),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "مشاهده یا ویرایش",
                                    style = MaterialTheme.typography.h2,
                                )
                            },
                            actions = {
                                Switch(
                                    checked = checkedEdit,
                                    onCheckedChange = { switch ->
                                        checkedEdit = switch
                                    },
                                    colors = SwitchDefaults.colors(MaterialTheme.colors.primaryVariant)
                                )
                                val textColor = if (checkedEdit) red else Color.Gray
                                Text(
                                    text = stringResource(R.string.edit),
                                    style = MaterialTheme.typography.body2,
                                    color = textColor
                                )
                            },
                            backgroundColor = MaterialTheme.colors.background,
                            elevation = 0.dp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                ) { its ->
                    val verticalScrollState = rememberScrollState(0)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .scrollable(
                                orientation = Orientation.Vertical,
                                state = verticalScrollState,
                                enabled = false
                            )
                            .padding(its)
                    ) {
                        Divider(
                            color = MaterialTheme.colors.onBackground,
                            thickness = 0.5.dp,
                            modifier = Modifier
                        )

                        OutlinedTextField(
                            value = topicValue,
                            onValueChange = { newTopic ->
                                topicValue = newTopic
                            },
                            isError = topicValue.isEmpty(),
                            label = { Text(text = stringResource(R.string.note_topic)) },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(
                                            AnnotatedString(note.topic)
                                        )
                                        Toast.makeText(
                                            context,
                                            "موضوع کپی شد.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_copy),
                                        contentDescription = "copy",
                                        tint = Color.Black,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            },
                            enabled = checkedEdit,
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = MaterialTheme.colors.primaryVariant,
                                focusedLabelColor = MaterialTheme.colors.primaryVariant,
                                textColor = MaterialTheme.colors.onBackground
                            ),
                            modifier = Modifier
                        )
                        OutlinedTextField(
                            value = descriptionValue,
                            onValueChange = { newDescription ->
                                descriptionValue = newDescription
                            },
                            label = { Text(text = stringResource(R.string.note_description)) },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(
                                            AnnotatedString(note.description)
                                        )
                                        Toast.makeText(
                                            context,
                                            "متن کپی شد.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_copy),
                                        contentDescription = "copy",
                                        tint = Color.Black,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            },
                            enabled = checkedEdit,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = MaterialTheme.colors.primaryVariant,
                                focusedLabelColor = MaterialTheme.colors.primaryVariant,
                                textColor = MaterialTheme.colors.onBackground
                            ),
                            modifier = Modifier
                        )
                    }
                }
            },
            title = {},
            backgroundColor = MaterialTheme.colors.background,
            modifier = Modifier
        )
    }
}

@Composable
fun DeleteNoteDialog(
    viewModel: NoteScreenViewModel,
    note: NoteEntity,
    action: (Boolean) -> Unit
) {
    var isDialog by remember { mutableStateOf(true) }
    AlertDialog(
        onDismissRequest = {
            isDialog = false
            action(false)
        },
        title = {
            Text(
                text = stringResource(R.string.delete_note),
                style = MaterialTheme.typography.h2,
            )
        },
        text = {
            Text(
                text = stringResource(R.string.are_you_sure),
                style = MaterialTheme.typography.h2,
            )
        },
        buttons = {
            var timer by remember { mutableStateOf(3) }
            var isClickable by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                while (timer > 0) {
                    delay(1000)
                    timer -= 1
                }
                isClickable = true
            }
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        isDialog = false
                        action(false)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = MaterialTheme.typography.button
                    )
                }
                Button(
                    enabled = isClickable,
                    onClick = {
                        viewModel.deleteNote(note)
                        isDialog = false
                        action(false)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    if (timer > 0) {
                        Text(
                            text = stringResource(
                                R.string.delete_confirm_timer,
                                timer
                            ),
                            style = MaterialTheme.typography.button
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.delete_confirm),
                            style = MaterialTheme.typography.button
                        )
                    }
                }
            }
        }
    )
}
