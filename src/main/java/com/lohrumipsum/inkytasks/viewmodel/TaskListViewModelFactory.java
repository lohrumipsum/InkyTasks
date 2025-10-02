package com.lohrumipsum.inkytasks.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TaskListViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final long taskListId;

    public TaskListViewModelFactory(Application application, long taskListId) {
        this.application = application;
        this.taskListId = taskListId;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TaskListViewModel.class)) {
            return (T) new TaskListViewModel(application, taskListId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

