package mohammadreza.ghavidel.noteapp.ui.screens.notes

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mohammadreza.ghavidel.noteapp.R
import mohammadreza.ghavidel.noteapp.database.notes.NoteEntity
import mohammadreza.ghavidel.noteapp.ui.common.deleteDialogTimer
import mohammadreza.ghavidel.noteapp.ui.theme.red


@Composable
fun EditOrShowDialog(
    viewModel: NoteScreenViewModel = hiltViewModel(),
    isShowNoteDialog: Boolean,
    note: NoteEntity,
    scaffoldState: ScaffoldState,
    action: (Boolean) -> Unit
) {
    var isShowNoteDialog1 = isShowNoteDialog
    if (isShowNoteDialog1) {
        var checkedEdit by rememberSaveable { mutableStateOf(false) }
        var topicValue by rememberSaveable { mutableStateOf(note.topic) }
        var descriptionValue by rememberSaveable { mutableStateOf(note.description) }
        val clipboardManager = LocalClipboardManager.current
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val enabled =
            (topicValue != note.topic || descriptionValue != note.description) && topicValue.isNotEmpty()

        AlertDialog(
            onDismissRequest = { },
            buttons = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        enabled = enabled,
                        onClick = {
                            isShowNoteDialog1 = false
                            action(false)
                            viewModel.updateNote(
                                topic = topicValue,
                                description = descriptionValue,
                                id = note.id
                            )
                            scope.launch {
                                delay(100)
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = "ویرایش انجام شد.",
                                    actionLabel = "ویرایش"
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = red
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp, bottom = 16.dp)
                    ) {
                        Text(
                            text = "ذخیره و بستن",
                            style = MaterialTheme.typography.button,
                            color = if (enabled) MaterialTheme.colors.background else Color.LightGray
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "مشاهده یا ویرایش",
                            style = MaterialTheme.typography.h2,
                        )
                        Spacer(modifier = Modifier.weight(1f))
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
                    }
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
                        readOnly = !checkedEdit,
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
                        readOnly = !checkedEdit,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colors.primaryVariant,
                            focusedLabelColor = MaterialTheme.colors.primaryVariant,
                            textColor = MaterialTheme.colors.onBackground
                        ),
                        modifier = Modifier
                    )
                }
            }
        )
    }
}

@Composable
fun DeleteNoteDialog(
    viewModel: NoteScreenViewModel = hiltViewModel(),
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
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.delete_note),
                    style = MaterialTheme.typography.h2,
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.are_you_sure),
                    style = MaterialTheme.typography.h2,
                )
            }
        },
        buttons = {
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
                    //enabled = isClickable,
                    onClick = {
                        viewModel.deleteNote(note)
                        isDialog = false
                        action(false)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.delete_confirm),
                        style = MaterialTheme.typography.button
                    )
                }
            }
        }
    )
}


@Composable
fun DeleteAllNotesDialog(
    viewModel: NoteScreenViewModel = hiltViewModel(),
    isDeleteAllDialog: Boolean,
    action: (Boolean) -> Unit
) {
    var isDeleteAllDialog1 = isDeleteAllDialog
    if (isDeleteAllDialog1) {
        AlertDialog(
            onDismissRequest = {
                isDeleteAllDialog1 = false
                action(false)
            },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.delete_all_notes),
                        style = MaterialTheme.typography.h2,
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.are_you_sure),
                        style = MaterialTheme.typography.h2,
                    )
                }
            },
            buttons = {
                var timer by rememberSaveable { mutableStateOf(deleteDialogTimer) }
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
                            isDeleteAllDialog1 = false
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
                            viewModel.deleteAllNotes()
                            isDeleteAllDialog1 = false
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
}