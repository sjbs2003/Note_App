package com.example.noteapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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

    NavHost(
        navController = navController,
        startDestination = NoteScreen.NoteList.route
        ) {
        composable(route = NoteScreen.NoteList.route) {
            NoteListScreen(
                onNoteClick = { nodeId ->
                    navController.navigate(NoteScreen.NoteDetail.route.replace("{noteId}", nodeId.toString()))
                }
            )
        }

        composable(
            route = NoteScreen.NoteDetail.route,
            arguments = listOf(navArgument("noteId"){ type = NavType.IntType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId")?: return@composable
            NoteDetailScreen(
                noteId = noteId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = NoteScreen.NoteCreate.route) {
            NoteCreationScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
