package com.lohrumipsum.inkytasks.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.lohrumipsum.inkytasks.database.AppDatabase;
import com.lohrumipsum.inkytasks.database.TaskDao;
import com.lohrumipsum.inkytasks.model.Task;
import com.lohrumipsum.inkytasks.model.TaskList;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TasksRepository {
    private final TaskDao taskDao;
    private final ExecutorService executorService;

    public TasksRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        taskDao = database.taskDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<TaskList>> getAllTaskLists() {
        return taskDao.getAllTaskLists();
    }

    public void addTaskList(String title) {
        executorService.execute(() -> {
            TaskList newTaskList = new TaskList();
            newTaskList.setTitle(title);
            taskDao.insertTaskList(newTaskList);
        });
    }

    public void updateTaskList(TaskList taskList) {
        executorService.execute(() -> taskDao.updateTaskList(taskList));
    }

    public void deleteTaskList(TaskList taskList) {
        executorService.execute(() -> {
            // First delete all tasks in the list, then delete the list itself
            taskDao.deleteTasksByListId(taskList.getId());
            taskDao.deleteTaskList(taskList);
        });
    }

    public LiveData<List<Task>> getTasksForList(long taskListId) {
        return taskDao.getTasksForList(taskListId);
    }

    public void addTask(String title, long taskListId) {
        executorService.execute(() -> {
            int maxPosition = taskDao.getMaxPosition(taskListId);
            Task newTask = new Task();
            newTask.setTitle(title);
            newTask.setTaskListId(taskListId);
            newTask.setStatus("needsAction");
            newTask.setPosition(maxPosition + 1);
            taskDao.insertTask(newTask);
        });
    }

    public void updateTask(Task task) {
        executorService.execute(() -> taskDao.updateTask(task));
    }

    public void deleteTask(Task task) {
        executorService.execute(() -> taskDao.deleteTask(task));
    }

    public void moveTask(List<Task> currentList, int fromPosition, int toPosition) {
        if (fromPosition < 0 || toPosition < 0 || fromPosition >= currentList.size() || toPosition >= currentList.size()) {
            return; // out of bounds
        }

        executorService.execute(() -> {
            Task taskFrom = currentList.get(fromPosition);
            Task taskTo = currentList.get(toPosition);

            // Swap their position values
            int tempPosition = taskFrom.getPosition();
            taskFrom.setPosition(taskTo.getPosition());
            taskTo.setPosition(tempPosition);

            // Update both tasks in the database in a single transaction
            taskDao.updateTasks(taskFrom, taskTo);
        });
    }

    public void clearCompletedTasks(long taskListId) {
        executorService.execute(() -> taskDao.clearCompletedTasks(taskListId));
    }
}

