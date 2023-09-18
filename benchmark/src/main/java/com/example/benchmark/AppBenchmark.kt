package com.example.benchmark

import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * This is an example startup benchmark.
 *
 * It navigates to the device's home screen, and launches the default activity.
 *
 * Before running this benchmark:
 * 1) switch your app's active build variant in the Studio (affects Studio runs only)
 * 2) add `<profileable android:shell="true" />` to your app's manifest, within the `<application>` tag
 *
 * Run this benchmark from Studio to see startup measurements, and captured system traces
 * for investigating your app's performance.
 */
@RunWith(AndroidJUnit4::class)
class AppBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startup() = benchmarkRule.measureRepeated(
        packageName = "com.example.to_docompose",
        metrics = listOf(StartupTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.WARM
    ) {
        pressHome()
        startActivityAndWait()
    }


    @Test
    fun addTask() = benchmarkRule.measureRepeated(
        packageName = "com.example.to_docompose",
        metrics = listOf(FrameTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.WARM
    ) {
        pressHome()
        startActivityAndWait()
        addTaskOperation()
    }

    private fun MacrobenchmarkScope.addTaskOperation() {
        val title = "My Title"
        val description = "My Description"

        device.wait(Until.hasObject(By.res("addTaskButtonTag")), 1000)
        device.findObject(By.res("addTaskButtonTag")).click()
        device.wait(Until.hasObject(By.res("titleInputTag")), 1000)

        device.findObject(By.res("titleInputTag")).text = title

        device.wait(Until.hasObject(By.res("descriptionInputTag")), 1000)
        device.findObject(By.res("descriptionInputTag")).text = description

        device.wait(Until.hasObject(By.res("addTaskButtonFormTag")), 1000)
        device.findObject(By.res("addTaskButtonFormTag")).click()

    }


    @Test
    fun editTask() = benchmarkRule.measureRepeated(
        packageName = "com.example.to_docompose",
        metrics = listOf(FrameTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.WARM
    ) {
        pressHome()
        startActivityAndWait()
        editTaskOperation()
    }

    private fun MacrobenchmarkScope.editTaskOperation() {
        val title = "My Title"
        val newTitle = "My New Title"
        val newDescription = "My New Description"

        addTaskOperation()

        device.wait(Until.hasObject(By.text(title)), 3000)
        device.findObject(By.text(title)).click()

        device.wait(Until.hasObject(By.res("descriptionInputTag")), 1000)
        device.findObject(By.res("descriptionInputTag")).text = newDescription

        device.wait(Until.hasObject(By.res("titleInputTag")), 1000)
        device.findObject(By.res("titleInputTag")).text = newTitle

        device.wait(Until.hasObject(By.res("ediTaskButtonFormTag")), 1000)
        device.findObject(By.res("ediTaskButtonFormTag")).click()

    }

    @Test
    fun deleteTask() = benchmarkRule.measureRepeated(
        packageName = "com.example.to_docompose",
        metrics = listOf(FrameTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.WARM
    ) {
        pressHome()
        startActivityAndWait()
        deleteTaskOperation()
    }

    private fun MacrobenchmarkScope.deleteTaskOperation() {

        addTaskOperation()
        val title = "My Title"

        device.wait(Until.hasObject(By.text(title)), 1000)
        device.findObject(By.text(title)).click()

        device.wait(Until.hasObject(By.res("titleInputTag")), 1000)

        device.wait(Until.hasObject(By.res("deleteTaskButtonFormTag")), 1000)
        device.findObject(By.res("deleteTaskButtonFormTag")).click()

        device.wait(Until.hasObject(By.text("Yes")), 1000)
        device.findObject(By.text("Yes")).click()

        device.wait(Until.hasObject(By.text("Tasks")), 3000)
    }


    @Test
    fun searchTask() = benchmarkRule.measureRepeated(
        packageName = "com.example.to_docompose",
        metrics = listOf(FrameTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.WARM
    ) {
        pressHome()
        startActivityAndWait()
        searchTaskOperation()
    }

    private fun MacrobenchmarkScope.searchTaskOperation() {

        addTaskOperation()
        val title = "My Title"

        device.wait(Until.hasObject(By.res("searchTaskFormButtonTag")), 1000)
        device.findObject(By.res("searchTaskFormButtonTag")).click()

        device.wait(Until.hasObject(By.res("searchTextFieldTag")), 1000)
        device.findObject(By.res("searchTextFieldTag")).click()
        device.findObject(By.res("searchTextFieldTag")).text + title

        /**TODO: Find a way to implement the Imeaction on keyboard**/
    }

    @Test
    fun filterTasks() = benchmarkRule.measureRepeated(
        packageName = "com.example.to_docompose",
        metrics = listOf(FrameTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.WARM
    ) {
        pressHome()
        startActivityAndWait()
        filterTasksOperation()
    }

    private fun MacrobenchmarkScope.filterTasksOperation() {

        addTaskOperation()
        device.wait(Until.hasObject(By.res("filterByPriorityFormTag")), 1000)
        device.findObject(By.res("filterByPriorityFormTag")).click()

        device.wait(Until.hasObject(By.text("HIGH")), 1000)

        device.findObject(By.text("HIGH")).click()

        device.wait(Until.hasObject(By.text("My Title")), 1000)

    }

    @Test
    fun showTaskDetails() = benchmarkRule.measureRepeated(
        packageName = "com.example.to_docompose",
        metrics = listOf(FrameTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.WARM
    ) {
        pressHome()
        startActivityAndWait()
        showTaskDetailsOperation()
    }

    private fun MacrobenchmarkScope.showTaskDetailsOperation() {
        addTaskOperation()
        val title = "My Title"

        device.wait(Until.hasObject(By.text(title)), 1000)
        device.findObject(By.text(title)).click()

        device.wait(Until.hasObject(By.text(title)), 1000)

    }
}