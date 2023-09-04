package com.example.to_docompose.ui.navigation

import androidx.navigation.NavHostController
import com.example.to_docompose.util.ActionLabels
import com.example.to_docompose.util.Constants.LIST_SCREEN

class Screens(navController: NavHostController) {
    val task: (ActionLabels) -> Unit = { action ->
        navController.navigate("list/${action.name}") {
            popUpTo(LIST_SCREEN) { inclusive = true }
        }
    }

    val list: (Int) -> Unit = { taskId ->
        navController.navigate("task/$taskId")
    }
}
