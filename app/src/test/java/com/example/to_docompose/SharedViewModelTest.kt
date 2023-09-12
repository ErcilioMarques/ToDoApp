package com.example.to_docompose

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.to_docompose.domain.TaskReducer
import com.example.to_docompose.domain.TasksStore
import com.example.to_docompose.domain.models.Priority
import com.example.to_docompose.domain.models.TaskViewState
import com.example.to_docompose.domain.models.ToDoTask
import com.example.to_docompose.repositories.FakeDataStoreRepository
import com.example.to_docompose.repositories.FakeToDoTaskRepository
import com.example.to_docompose.ui.viewmodels.SharedViewModel
import com.example.to_docompose.util.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SharedViewModelTest {

    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    private lateinit var viewModel: SharedViewModel
    private lateinit var testDispatcher: TestCoroutineDispatcher

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        testDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testDispatcher) // Set the Main dispatcher for testing

        viewModel = SharedViewModel(
            store = TasksStore(
                initialState = TaskViewState(),
                repository = FakeToDoTaskRepository(),
                dataStoreRepository = FakeDataStoreRepository(),
                reducer = TaskReducer(),
            )
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `insert todoTask item with empty fields, returns error`() = testDispatcher.runBlockingTest {
        viewModel.updateTitle("")
        viewModel.updateDescription("")
        viewModel.updatePriority(Priority.LOW)
        viewModel.addTask()
        viewModel.getAllTasks()

        assert((viewModel.viewState.value.allTasks as RequestState.Success<List<ToDoTask>>).data == listOf<ToDoTask>())
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `delete all todoTasks, tasks list should not have data`() = testDispatcher.runBlockingTest  {

        launch {
            runBlocking {
                viewModel.updateTitle("MyTitle")
                viewModel.updateDescription("My Description")
                viewModel.updatePriority(Priority.LOW)
                viewModel.addTask()
                viewModel.deleteAllTasks()
                viewModel.getAllTasks()
            }}
        assert((viewModel.viewState.value.allTasks as RequestState.Success<List<ToDoTask>>).data.isEmpty())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `update title todoTask field with actual data, returns actual data`() = testDispatcher.runBlockingTest  {
        val value = "mytitle"
        launch {
            runBlocking {
                viewModel.updateTitle(value)
                println("Comparacoes -> ${viewModel.viewState.value.title } == ${value}")
            }
        }

        assert(viewModel.viewState.value.title == value)
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `update a todoTask, tasks list should have the new task data`() = testDispatcher.runBlockingTest  {
        var oldTask: ToDoTask? = null

        launch {
            runBlocking {
                viewModel.deleteAllTasks()
                viewModel.updateTitle("MyTitle")
                viewModel.updateDescription("My Description")
                viewModel.updatePriority(Priority.LOW)
                viewModel.addTask()
                viewModel.getSelectedTask(1)

                oldTask = viewModel.viewState.value.selectedTask
                viewModel.updateTaskFields(
                    oldTask?.copy(
                        title = "NewTitle",
                        description = "NewDescription",
                        priority = Priority.HIGH
                    )
                )

                viewModel.updateTask()

                viewModel.getAllTasks()
            }}

        assert(
            (viewModel.viewState.value.allTasks as RequestState.Success<List<ToDoTask>>).data[0].title != oldTask?.title
                    && (viewModel.viewState.value.allTasks as RequestState.Success<List<ToDoTask>>).data[0].description != oldTask?.description
                    && (viewModel.viewState.value.allTasks as RequestState.Success<List<ToDoTask>>).data[0].priority != oldTask?.priority
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `update description todoTask field with actual data, returns actual data`() =
        runBlockingTest {
            val value = "mydescription"
            launch {
                runBlocking {
                    viewModel.updateTaskFields(null)
                    viewModel.updateDescription(value)
                }
            }
            assert(viewModel.viewState.value.description == value)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `update priority todoTask field with actual data, returns actual data`() = testDispatcher.runBlockingTest  {
        val value = Priority.HIGH

        viewModel.updatePriority(value)
        assert(viewModel.viewState.value.priority == value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `delete todoTask, tasks list should not have that task`() = testDispatcher.runBlockingTest  {
        val task = ToDoTask(
            id = 1,
            title = "MyTitle1",
            description = "MyDescription1",
            priority = Priority.HIGH
        )

        launch {
            runBlocking {
                viewModel.updateTitle(task.title)
                viewModel.updateDescription(task.description)
                viewModel.updatePriority(task.priority)
                viewModel.addTask()
                viewModel.updateTaskFields(task)
                viewModel.deleteTask()
            }

            runBlocking {
                viewModel.getAllTasks()
            }
        }
        assert((viewModel.viewState.value.allTasks as RequestState.Success<List<ToDoTask>>).data.none { it.id == task.id })
    }
}
