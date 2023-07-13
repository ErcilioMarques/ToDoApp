package com.example.to_docompose.ui.screens.list

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ListAppBar() {
    DefaultListAppbar()
}

@Composable
fun DefaultListAppbar() {
    TopAppBar(
        title = {
            Text(text = "Tasks")
        },
        backgroundColor = MaterialTheme.colors.primary,
    )
}

@Composable
@Preview
private fun DefaultListAppBarPreview() {
    DefaultListAppbar()
}
