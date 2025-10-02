package com.lohrumipsum.inkytasks.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_lists")
public class TaskList {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;

    // Room requires a no-arg constructor
    public TaskList() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

