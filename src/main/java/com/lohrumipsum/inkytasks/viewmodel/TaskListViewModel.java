package com.lohrumipsum.inkytasks.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.lohrumipsum.inkytasks.model.Task;
import com.lohrumipsum.inkytasks.repository.TasksRepository;
import java.util.List;

public class TaskListViewModel extends AndroidViewModel {
    private final TasksRepository repository;
    private final LiveData<List<Task>> tasks;
    private final long taskListId;

    public TaskListViewModel(@NonNull Application application, long taskListId) {
        super(application);
        this.taskListId = taskListId;
        this.repository = new TasksRepository(application);
        this.tasks = repository.getTasksForList(taskListId);
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public void addTask(String title) {
        repository.addTask(title, taskListId);
    }

    public void updateTask(Task task) {
        repository.updateTask(task);
    }

    public void deleteTask(Task task) {
        repository.deleteTask(task);
    }

    public void clearCompletedTasks() {
        repository.clearCompletedTasks(taskListId);
    }

    public void moveTask(List<Task> currentList, int fromPosition, int toPosition) {
        repository.moveTask(currentList, fromPosition, toPosition);
    }
}

