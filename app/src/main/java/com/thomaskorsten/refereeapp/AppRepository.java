package com.thomaskorsten.refereeapp;

import android.app.Application;

import androidx.room.Room;

import com.thomaskorsten.refereeapp.dao.PenaltyDao;
import com.thomaskorsten.refereeapp.models.Penalty;

import java.util.List;

public class AppRepository {
    private PenaltyDao penaltyDao;
    private List<Penalty> penaltyList;

    AppRepository(Application application) {

        AppDatabase db = AppDatabase.getDatabase(application);
        /*
        Dieser Aufruf funktioniert nicht, da es auf dem Mainthread eine Query startet aber dies von
        Rooms nicht gestattet wird. Hier wird es mit AllowOnMainThread umgangen.
        */
        //AppDatabase db = Room.databaseBuilder(application, AppDatabase.class, "app_database").allowMainThreadQueries().build();
        penaltyDao = db.penaltyDao();
        penaltyList = penaltyDao.getAll();
    }

    void insert(Penalty... penalties) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            penaltyDao.insertPenalties(penalties);
        });
    }

    List<Penalty> getPenaltyList() {
        return penaltyList;
    }

    void reinit() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            penaltyDao.deleteAll();

            penaltyDao.insertPenalties(new Penalty("Foul0", 10, 0, false, true, ""));
            penaltyDao.insertPenalties(new Penalty("Foul1", 5, 0));
            penaltyDao.insertPenalties(new Penalty("Foul2", 15, 0));
            penaltyDao.insertPenalties(new Penalty("Foul3", 10, 0, true, false, ""));
        });
    }
}
