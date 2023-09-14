package com.example.to_docompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.to_docompose.ui.navigation.destinations.listComposable
import com.example.to_docompose.ui.navigation.destinations.taskComposable
import com.example.to_docompose.ui.viewmodels.SharedViewModel
import com.example.to_docompose.util.Constants.LIST_SCREEN

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SetupNavigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val screen = remember(navController) {
        Screens(navController)
    }
    NavHost(navController = navController, startDestination = LIST_SCREEN, modifier = Modifier.semantics {
        testTagsAsResourceId = true
    }) {
        listComposable(navigateToTaskScreen = screen.list, sharedViewModel= sharedViewModel)
        taskComposable(navigateToListScreen = screen.task, sharedViewModel = sharedViewModel)
    }
}
