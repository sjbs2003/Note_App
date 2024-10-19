package com.example.noteapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.noteapp.data.room.NoteDatabase
import com.example.noteapp.data.room.OfflineNoteRepository
import com.example.noteapp.ui.screens.NoteCreationScreen
import com.example.noteapp.ui.screens.NoteDetailScreen
import com.example.noteapp.ui.screens.NoteListScreen


enum class NoteScreen(val route: String) {
    NoteList("noteList"),
    NoteDetail("noteDetail/{noteId}"),
    NoteCreate("noteCreate/{noteId}")
}

@Composable
fun NoteApp() {
    val navController = rememberNavController()
    val database = NoteDatabase.getDatabase(LocalContext.current)
    val noteDao = database.noteDao()
    val repository = OfflineNoteRepository(noteDao)

    NavHost(
        navController = navController,
        startDestination = NoteScreen.NoteList.route
        ) {
        composable(route = NoteScreen.NoteList.route) {
            NoteListScreen(
                repository = repository,
                onNoteClick = { nodeId ->
                    navController.navigate(NoteScreen.NoteDetail.route.replace("{noteId}", nodeId.toString()))
                },
                onCreateNoteClick = {
                    navController.navigate(NoteScreen.NoteCreate.route)
                }
            )
        }

        composable(
            route = NoteScreen.NoteDetail.route,
            arguments = listOf(navArgument("noteId"){ type = NavType.LongType }) // NavType is LongType cause noteId is long
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId")?: return@composable
            NoteDetailScreen(
                repository = repository,
                noteId = noteId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = NoteScreen.NoteCreate.route) {
            NoteCreationScreen(
                repository = repository,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
