package com.example.noteapp

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.noteapp.data.room.NoteViewModelFactory
import com.example.noteapp.ui.NoteCreationScreen
import com.example.noteapp.ui.NoteDetailScreen
import com.example.noteapp.ui.NoteListScreen
import com.example.noteapp.ui.NoteViewModel

enum class NoteScreen(val route: String) {
    NoteList("noteList"),
    NoteDetail("noteDetail/{noteId}"),
    NoteCreate("noteCreate/{noteId}"),
    NoteCreateNew("noteCreateNew")
}

@Composable
fun NoteAppScreen(
    navController: NavHostController,
    viewModelFactory: NoteViewModelFactory
) {
    val viewModel: NoteViewModel = viewModel(factory = viewModelFactory)
    NavHost(navController = navController, startDestination = NoteScreen.NoteList.route) {
        composable(route = NoteScreen.NoteList.route) {
            NoteListScreen(
                notes = viewModel.notes.value,
                onNoteClick = {
                    navController.navigate(NoteScreen.NoteDetail.route.replace("{noteId}", it.id.toString()))
                },
                onAddNoteClick = {
                    navController.navigate(NoteScreen.NoteCreateNew.route)
                }
            )
        }

        composable(route = NoteScreen.NoteDetail.route) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull()
            val note = noteId?.let { viewModel.getNoteById(it) }
            if (note != null) {
                NoteDetailScreen(
                    note = note,
                    onEditClick = {
                        viewModel.saveOrUpdateNote(note)
                        navController.popBackStack()
                                  },
                    onDeleteClick = {
                        viewModel.deleteNote(note)
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(route = NoteScreen.NoteCreate.route) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull()
            val note = noteId?.let { viewModel.getNoteById(it) }
            NoteCreationScreen(
                note = note,
                onSaveClick = {
                    viewModel.saveOrUpdateNote(it)
                    navController.popBackStack()
                }
            )
        }

        composable(route = NoteScreen.NoteCreateNew.route) {
            NoteCreationScreen(
                note = null,
                onSaveClick = {
                    viewModel.saveOrUpdateNote(it)
                    navController.popBackStack()
                }
            )
        }
    }
}
