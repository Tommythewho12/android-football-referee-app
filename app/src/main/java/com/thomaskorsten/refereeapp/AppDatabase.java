package com.thomaskorsten.refereeapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.thomaskorsten.refereeapp.dao.PenaltyDao;
import com.thomaskorsten.refereeapp.models.Penalty;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

@Database(entities = {Penalty.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    abstract PenaltyDao penaltyDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 2;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app_database").allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}
