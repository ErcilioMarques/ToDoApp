package com.example.to_docompose.ui.screens.task

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.to_docompose.R
import com.example.to_docompose.ui.components.PriorityDropDown
import com.example.to_docompose.domain.models.Priority
import com.example.to_docompose.domain.models.TaskViewState
import com.example.to_docompose.ui.theme.LARGE_PADDING
import com.example.to_docompose.ui.theme.MEDIUM_PADDING
import com.example.to_docompose.ui.viewmodels.SharedViewModel

@Composable
fun TaskContent(
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriorityChange: (Priority) -> Unit,
    viewState: TaskViewState
) {

    Log.d("viewState", "viewState -> ${viewState}")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(all = LARGE_PADDING)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewState.title,
            onValueChange = { onTitleChange(it) },
            label = {
                Text(
                    text = stringResource(R.string.title)
                )
            },
            textStyle = MaterialTheme.typography.body1,
            singleLine = true
        )

        Divider(modifier = Modifier.height(MEDIUM_PADDING), color = MaterialTheme.colors.background)

        PriorityDropDown(priority = viewState.priority, onPrioritySelected = onPriorityChange)

        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = viewState.description,
            onValueChange = { onDescriptionChange(it) },
            label = {
                Text(
                    text = stringResource(R.string.description)
                )
            },
            textStyle = MaterialTheme.typography.body1,
        )

    }
}

//@Composable
//@Preview
//fun TaskContentPreview(){
//    TaskContent(
//        title = "",
//        onTitleChange = {},
//        description = "",
//        onDescriptionChange = {},
//        priority = Priority.LOW,
//        onPriorityChange = {}
//    )
//}