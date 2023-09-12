package com.example.to_docompose

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.to_docompose.data.models.Priority
import com.example.to_docompose.data.models.ToDoTask
import com.example.to_docompose.repositories.FakeDataStoreRepository
import com.example.to_docompose.repositories.FakeToDoTaskRepository
import com.example.to_docompose.ui.viewmodels.SharedViewModel
import com.example.to_docompose.util.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SharedViewModelTest {

    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    private lateinit var viewModel: SharedViewModel
    private lateinit var testDispatcher: TestCoroutineDispatcher

    @Before
    fun setup() {
        testDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testDispatcher) // Set the Main dispatcher for testing

        viewModel = SharedViewModel(
            repository = FakeToDoTaskRepository(),
            dataStoreRepository = FakeDataStoreRepository()
        )
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `insert todoTask item with empty fields, returns error`() = testDispatcher.runBlockingTest {
        viewModel.updateTitle("")
        viewModel.updateDescription("")
        viewModel.updatePriority(Priority.LOW)
        viewModel.addTask()
        viewModel.getAllTasks()

        assert((viewModel.allTasks.value as RequestState.Success<List<ToDoTask>>).data == listOf<ToDoTask>())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `update title todoTask field with actual data, returns actual data`() =
        testDispatcher.runBlockingTest {
            val value = "mytitle"
            viewModel.updateTitle(value)
            assert(viewModel.title == value)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `update description todoTask field with actual data, returns actual data`() =
        testDispatcher.runBlockingTest {
            val value = "mydescription"
            viewModel.updateDescription(value)
            assert(viewModel.description == value)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `update priority todoTask field with actual data, returns actual data`() =
        testDispatcher.runBlockingTest {
            val value = Priority.HIGH
            viewModel.updatePriority(value)
            assert(viewModel.priority == value)
        }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `delete todoTask, tasks list should not have that task`() = testDispatcher.runBlockingTest {
        val task = ToDoTask(id = 1, title="MyTitle1", description = "MyDescription1", priority = Priority.HIGH)
        viewModel.updateTitle(task.title)
        viewModel.updateDescription(task.description)
        viewModel.updatePriority(task.priority)
        viewModel.addTask()
        viewModel.updateTaskFields(task)
        viewModel.deleteTask()
        viewModel.getAllTasks()

        println("Task -> ${(viewModel.allTasks.value as RequestState.Success<List<ToDoTask>>).data}")
        assert((viewModel.allTasks.value as RequestState.Success<List<ToDoTask>>).data.none{ it -> it.id == task.id })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `delete all todoTasks, tasks list should not have data`() = testDispatcher.runBlockingTest {
        viewModel.updateTitle("MyTitle")
        viewModel.updateDescription("My Description")
        viewModel.updatePriority(Priority.LOW)
        viewModel.addTask()
        viewModel.deleteAllTasks()
        viewModel.getAllTasks()

        assert((viewModel.allTasks.value as RequestState.Success<List<ToDoTask>>).data == listOf<ToDoTask>())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `update a todoTask, tasks list should have the new task data`() =
        testDispatcher.runBlockingTest {
            viewModel.updateTitle("MyTitle")
            viewModel.updateDescription("My Description")
            viewModel.updatePriority(Priority.LOW)
            viewModel.addTask()
            viewModel.getSelectedTask(1)
            val oldTask = viewModel.selectedTask.value

            viewModel.updateTaskFields(
                oldTask?.copy(
                    title = "NewTitle",
                    description = "NewDescription",
                    priority = Priority.HIGH
                )
            )
            viewModel.updateTask()

            viewModel.getAllTasks()

            assert(
                (viewModel.allTasks.value as RequestState.Success<List<ToDoTask>>).data.get(0).title != oldTask?.title
                        &&
                        (viewModel.allTasks.value as RequestState.Success<List<ToDoTask>>).data.get(0).description != oldTask?.description &&
                        (viewModel.allTasks.value as RequestState.Success<List<ToDoTask>>).data.get(0).priority != oldTask?.priority
            )
        }
}