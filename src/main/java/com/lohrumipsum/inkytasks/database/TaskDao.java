package com.lohrumipsum.inkytasks.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.lohrumipsum.inkytasks.model.Task;
import com.lohrumipsum.inkytasks.model.TaskList;
import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insertTaskList(TaskList taskList);

    @Update
    void updateTaskList(TaskList taskList);

    @Delete
    void deleteTaskList(TaskList taskList);

    @Query("SELECT * FROM task_lists")
    LiveData<List<TaskList>> getAllTaskLists();

    @Insert
    void insertTask(Task task);

    @Update
    void updateTask(Task task);

    @Update
    void updateTasks(Task... tasks);

    @Delete
    void deleteTask(Task task);

    @Query("DELETE FROM tasks WHERE taskListId = :taskListId")
    void deleteTasksByListId(long taskListId);

    @Query("SELECT * FROM tasks WHERE taskListId = :taskListId ORDER BY position ASC")
    LiveData<List<Task>> getTasksForList(long taskListId);

    @Query("DELETE FROM tasks WHERE taskListId = :taskListId AND status = 'completed'")
    void clearCompletedTasks(long taskListId);

    @Query("SELECT COALESCE(MAX(position), 0) FROM tasks WHERE taskListId = :taskListId")
    int getMaxPosition(long taskListId);
}

