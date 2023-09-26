package mohammadreza.ghavidel.noteapp.ui.screens.notes

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mohammadreza.ghavidel.noteapp.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NotesScreen(
    viewModel: NoteScreenViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val notes by viewModel.notes.collectAsState()
    val bookmarkNotes by viewModel.bookmarkNotes.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var isDeleteAllDialog by remember { mutableStateOf(false) }
    var bookmarkFilter by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetBackgroundColor = MaterialTheme.colors.background,
        sheetShape = MaterialTheme.shapes.large.copy(
            bottomStart = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp)
        ),
        sheetContent = {
            SheetContent(
                bottomSheetState = bottomSheetState,
                scope = scope,
                scaffoldState = scaffoldState,
                viewModel = viewModel
            )
        },
        sheetElevation = 5.dp,
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    title = {
                        val title = if (bookmarkFilter)
                            stringResource(R.string.liked_notes)
                        else
                            stringResource(R.string.notes_title)
                        Text(
                            text = title,
                            style = MaterialTheme.typography.h1,
                            color = MaterialTheme.colors.onBackground
                        )
                    },
                    actions = {
                        IconButton(onClick = {
                            bookmarkFilter = !bookmarkFilter
                        }) {
                            val bookmarkIcon = if (bookmarkFilter) {
                                Icons.Outlined.Favorite
                            } else {
                                Icons.Outlined.FavoriteBorder
                            }
                            Icon(
                                imageVector = bookmarkIcon,
                                contentDescription = "bookmarks list"
                            )
                        }
                        IconButton(
                            onClick = {
                                expanded = !expanded
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Drop Down Menu"
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .background(MaterialTheme.colors.background)
                                .clip(MaterialTheme.shapes.small)
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    isDeleteAllDialog = true
                                    expanded = false
                                },
                                enabled = notes.isNotEmpty(),
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "delete all"
                                )
                                Text(
                                    text = stringResource(R.string.delete_all_notes),
                                    style = MaterialTheme.typography.body1,
                                )
                            }
                        }
                        DeleteAllNotesDialog(isDeleteAllDialog, viewModel) { itDialog ->
                            isDeleteAllDialog = itDialog
                        }
                    },
                    backgroundColor = MaterialTheme.colors.background
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        scope.launch(Dispatchers.IO) {
                            bottomSheetState.show()
                        }
                    },
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "add new note"
                    )
                }
            },
        ) {
            LazyVerticalGrid(
                modifier = Modifier.padding(it),
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(
                    vertical = 16.dp,
                    horizontal = 24.dp
                )
            ) {
                items(
                    if (bookmarkFilter) bookmarkNotes else notes
                ) { note ->
                    var isShowNoteDialog by remember { mutableStateOf(false) }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .border(1.dp, MaterialTheme.colors.primary, MaterialTheme.shapes.medium)
                            .height(150.dp)
                            .clickable { isShowNoteDialog = true },
                        contentPadding = PaddingValues(
                            vertical = 8.dp,
                            horizontal = 8.dp
                        ),
                    ) {
                        item {
                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                val bookmarkIcon = if (note.isBookmarked) {
                                    Icons.Outlined.Favorite
                                } else {
                                    Icons.Outlined.FavoriteBorder
                                }
                                Icon(
                                    imageVector = bookmarkIcon,
                                    contentDescription = "bookmark note",
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            viewModel.doBookmark(note)
                                        }
                                )
                                Text(
                                    text = note.topic,
                                    style = MaterialTheme.typography.h2,
                                    color = MaterialTheme.colors.primaryVariant,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 4.dp)
                                )
                                var isDeleteNoteDialog by remember { mutableStateOf(false) }

                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "delete note",
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            isDeleteNoteDialog = true
                                        }
                                )
                                if (isDeleteNoteDialog) {
                                    DeleteNoteDialog(
                                        viewModel = viewModel,
                                        note = note
                                    ) { itDialog ->
                                        isDeleteNoteDialog = itDialog
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.size(12.dp))
                            Text(
                                text = stringResource(
                                    R.string.note_description_preview,
                                    note.description
                                ),
                                style = MaterialTheme.typography.body1,
                            )
                        }
                    }
                    EditOrShowDialog(
                        isShowNoteDialog,
                        note,
                        viewModel,
                        scope,
                        scaffoldState,
                        clipboardManager,
                        context
                    ) { itEditDialog ->
                        isShowNoteDialog = itEditDialog
                    }
                }
            }
            BackHandler(enabled = bottomSheetState.isVisible) {
                scope.launch {
                    bottomSheetState.hide()
                }
            }
        }
    }
}