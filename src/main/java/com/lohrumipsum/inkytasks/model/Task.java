package com.lohrumipsum.inkytasks.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks",
        foreignKeys = @ForeignKey(entity = TaskList.class,
                parentColumns = "id",
                childColumns = "taskListId",
                onDelete = ForeignKey.CASCADE))
public class Task {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String status; // "needsAction" or "completed"
    private int position; // For reordering

    @ColumnInfo(index = true)
    private long taskListId;

    public Task() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public long getTaskListId() { return taskListId; }
    public void setTaskListId(long taskListId) { this.taskListId = taskListId; }
}

