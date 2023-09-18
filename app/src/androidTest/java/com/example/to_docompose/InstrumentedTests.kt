package com.example.to_docompose

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.to_docompose.data.models.Priority
import com.example.to_docompose.data.models.ToDoTask
import com.example.to_docompose.navigation.SetupNavigation
import com.example.to_docompose.repositories.FakeDataStoreRepository
import com.example.to_docompose.repositories.FakeToDoTaskRepository
import com.example.to_docompose.ui.theme.ToDoComposeTheme
import com.example.to_docompose.ui.viewmodels.SharedViewModel
import com.example.to_docompose.util.Constants.LIST_SCREEN
import com.example.to_docompose.util.Constants.TASK_SCREEN
import com.example.to_docompose.util.RequestState
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InstrumentedTests {
    @get:Rule
    val rule = createComposeRule()

    private lateinit var viewModel: SharedViewModel
    private lateinit var navController: NavHostController

    @Before
    fun setUp() {
        viewModel = SharedViewModel(
            repository = FakeToDoTaskRepository(), dataStoreRepository = FakeDataStoreRepository()
        )
    }

    @Test
    fun addTask() {
        val title = "My Title"
        val description = "My Description"

        with(rule) {
            setContent {
                ToDoComposeTheme {
                    navController = rememberNavController()
                    SetupNavigation(navController, viewModel)
                }
            }
            assertThat(navController.currentBackStackEntry?.destination?.route).isEqualTo(
                LIST_SCREEN
            )

            onNodeWithContentDescription("Add Button").performClick()
            waitForIdle()
            assertThat(navController.currentBackStackEntry?.destination?.route).isEqualTo(
                TASK_SCREEN
            )
            onNodeWithText("Title").assertIsDisplayed()
            onNodeWithText("Description").assertIsDisplayed()

            onNodeWithTag("titleInputTag").performTextInput(title)
            onNodeWithTag("descriptionInputTag").performTextInput(description)

            onNodeWithText("Add Task").performClick()
            onNodeWithText(title).assertIsDisplayed()

        }
    }

    @Test
    fun editTask() {
        val newTitle = "My New Title"
        val newDescription = "My New Description"
        val task =
            ToDoTask(title = "MyTitle1", description = "MyDescription1", priority = Priority.HIGH)
        addTask(task)

        with(rule) {
            setContent {
                ToDoComposeTheme {
                    navController = rememberNavController()
                    SetupNavigation(navController, viewModel)
                }
            }
            assertThat(navController.currentBackStackEntry?.destination?.route).isEqualTo(
                LIST_SCREEN
            )
            onNodeWithText("MyTitle1").performClick()
            waitForIdle()
            assertThat(navController.currentBackStackEntry?.destination?.route).isEqualTo(
                TASK_SCREEN
            )
            onNodeWithText("Title").assertIsDisplayed()
            onNodeWithText("Description").assertIsDisplayed()

            onNodeWithTag("titleInputTag").performTextInput("")
            waitForIdle()
            onNodeWithTag("titleInputTag").performTextInput(newTitle)
            waitForIdle()
            onNodeWithTag("descriptionInputTag").performTextInput("")
            waitForIdle()
            onNodeWithTag("descriptionInputTag").performTextInput(newDescription)
            waitForIdle()
            onNodeWithContentDescription("Update Icon").performClick()
            waitForIdle()
            onNodeWithText(newDescription).assertIsDisplayed()

        }
    }

    @Test
    fun deleteTask() {
        val title = "My Title"
        val description = "My Description"
        val task = ToDoTask(title = title, description = description, priority = Priority.HIGH)
        addTask(task)

        with(rule) {
            setContent {
                ToDoComposeTheme {
                    navController = rememberNavController()
                    SetupNavigation(navController, viewModel)
                }
            }
            assertThat(navController.currentBackStackEntry?.destination?.route).isEqualTo(
                LIST_SCREEN
            )
            onNodeWithText(title).performClick()
            waitForIdle()
            assertThat(navController.currentBackStackEntry?.destination?.route).isEqualTo(
                TASK_SCREEN
            )
            onNodeWithText("Title").assertIsDisplayed()
            onNodeWithText("Description").assertIsDisplayed()

            onNodeWithContentDescription("Delete Icon").performClick()
            onNodeWithText("Yes").performClick()
            waitForIdle()
            assert((viewModel.allTasks.value as RequestState.Success<List<ToDoTask>>).data.none { it -> it.title == title })
        }
    }

    @Test
    fun searchTask() {
        val title = "My Title"
        val description = "My Description"
        val task = ToDoTask(title = title, description = description, priority = Priority.HIGH)
        addTask(task)

        with(rule) {
            setContent {
                ToDoComposeTheme {
                    navController = rememberNavController()
                    SetupNavigation(navController, viewModel)
                }
            }
            assertThat(navController.currentBackStackEntry?.destination?.route).isEqualTo(
                LIST_SCREEN
            )
            onNodeWithContentDescription("Search Tasks").performClick()
            val textField = onNodeWithText("Search")
            textField.performTextInput("My")
            onNodeWithTag("searchTextFieldTag").performImeAction()
            waitForIdle()
            assertThat(navController.currentBackStackEntry?.destination?.route).isEqualTo(
                LIST_SCREEN
            )

            onNodeWithText(title).assertIsDisplayed()
            assert((viewModel.searchedTasks.value as RequestState.Success<List<ToDoTask>>).data.find { it -> it.title != title } == null)
            assert((viewModel.searchedTasks.value as RequestState.Success<List<ToDoTask>>).data.find { it -> it.title == title } != null)

        }
    }

    @Test
    fun filterTasksByPriority() {
        val title = "My Title"
        val description = "My Description"
        val task = ToDoTask(title = title, description = description, priority = Priority.HIGH)
        addTask(task)

        with(rule) {
            setContent {
                ToDoComposeTheme {
                    navController = rememberNavController()
                    SetupNavigation(navController, viewModel)
                }
            }
            assertThat(navController.currentBackStackEntry?.destination?.route).isEqualTo(
                LIST_SCREEN
            )
            onNodeWithContentDescription("Sort actions").performClick()

            onNodeWithText("LOW").performClick()
            assert(viewModel.lowPriorityTasks.value != emptyList<ToDoTask>())

            waitForIdle()

            onNodeWithContentDescription("Sort actions").performClick()

            onNodeWithText("HIGH").performClick()
            assert(viewModel.highPriorityTasks.value != emptyList<ToDoTask>())


        }
    }

@Test
    fun showTaskDetails() {
        val title = "My Title"
        val description = "My Description"
        val task = ToDoTask(title = title, description = description, priority = Priority.HIGH)
        addTask(task)

        with(rule) {
            setContent {
                ToDoComposeTheme {
                    navController = rememberNavController()
                    SetupNavigation(navController, viewModel)
                }
            }
            assertThat(navController.currentBackStackEntry?.destination?.route).isEqualTo(
                LIST_SCREEN
            )

            onNodeWithText(title).assertIsDisplayed()
            onNodeWithText(title).performClick()
            waitForIdle()
            assertThat(navController.currentBackStackEntry?.destination?.route).isEqualTo(
                TASK_SCREEN
            )
            onNodeWithText("Title").assertIsDisplayed()
            onNodeWithText("Description").assertIsDisplayed()
        }
    }

    fun addTask(task: ToDoTask) {
        viewModel.updateTitle(task.title)
        viewModel.updateDescription(task.description)
        viewModel.updatePriority(task.priority)
        viewModel.addTask()

    }
}