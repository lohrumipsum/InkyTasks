package com.lohrumipsum.inkytasks.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.lohrumipsum.inkytasks.model.Task;
import com.lohrumipsum.inkytasks.model.TaskList;

@Database(entities = {TaskList.class, Task.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "inky_tasks_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

