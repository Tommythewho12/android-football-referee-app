package com.thomaskorsten.refereeapp.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.thomaskorsten.refereeapp.EnforcementSpot;

import static com.thomaskorsten.refereeapp.EnforcementSpot.BASIC;


@Entity(tableName = "penalties")
public class Penalty {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    @ColumnInfo(name = "name")
    public String name;

    @NonNull
    @ColumnInfo(name = "yards")
    public int yards;

    @NonNull
    @ColumnInfo(name = "spot")
    // 0 = basic, 1 = previous, 2 = succeeding, 3 = spot of foul
    public int spot;

    @NonNull
    @ColumnInfo(name = "automatic_first_down")
    public boolean automaticFirstDown;

    @NonNull
    @ColumnInfo(name = "loss_of_down")
    public boolean lossOfDown;

    @NonNull
    @ColumnInfo(name = "additional_info")
    public String additionalInfo;

    public Penalty(String name, int yards, int spot, boolean automaticFirstDown, boolean lossOfDown, String additionalInfo) {
        this.name = name;
        this.yards = yards;
        this.spot = spot;
        this.automaticFirstDown = automaticFirstDown;
        this.lossOfDown = lossOfDown;
        this.additionalInfo = additionalInfo;
    }

    @Ignore
    public Penalty(String name) {
        this(name, 15, 0, false, false, "");
    }

    @Ignore
    public Penalty(String name, int yards, int spot) {
        this(name, yards, spot, false, false, "");
    }
}
