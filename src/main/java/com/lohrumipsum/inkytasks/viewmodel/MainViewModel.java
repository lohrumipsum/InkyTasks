package com.lohrumipsum.inkytasks.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.lohrumipsum.inkytasks.model.TaskList;
import com.lohrumipsum.inkytasks.repository.TasksRepository;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private final TasksRepository repository;
    private final LiveData<List<TaskList>> taskLists;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new TasksRepository(application);
        taskLists = repository.getAllTaskLists();
    }

    public LiveData<List<TaskList>> getTaskLists() {
        return taskLists;
    }

    public void addTaskList(String title) {
        repository.addTaskList(title);
    }

    public void updateTaskList(TaskList taskList) {
        repository.updateTaskList(taskList);
    }

    public void deleteTaskList(TaskList taskList) {
        repository.deleteTaskList(taskList);
    }
}

