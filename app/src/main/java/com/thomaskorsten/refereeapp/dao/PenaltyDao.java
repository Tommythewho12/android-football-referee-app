package com.thomaskorsten.refereeapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.thomaskorsten.refereeapp.models.Penalty;

import java.util.List;

@Dao
public interface PenaltyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPenalties(Penalty... penalties);

    @Update
    void updatePenalties(Penalty... penalties);

    @Delete
    void deletePenalty(Penalty penalty);

    @Query("SELECT * FROM penalties")
    List<Penalty> getAll();

    @Query("DELETE FROM penalties")
    public void deleteAll();
}
