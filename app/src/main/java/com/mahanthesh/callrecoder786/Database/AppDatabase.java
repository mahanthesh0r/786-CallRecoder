package com.mahanthesh.callrecoder786.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.mahanthesh.callrecoder786.model.Task;
import com.mahanthesh.callrecoder786.model.TaskDao;

@Database(entities = {Task.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
