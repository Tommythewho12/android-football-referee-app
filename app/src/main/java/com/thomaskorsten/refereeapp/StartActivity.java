package com.thomaskorsten.refereeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.thomaskorsten.refereeapp.models.Penalty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
    }

    public void openCalculator(View view) {
        Intent intent = new Intent(this, CalculatorActivity.class);
        startActivity(intent);
    }

    public void openPenalties(View view) {
        Intent intent = new Intent(this, PenaltiesActivity.class);
        startActivity(intent);
    }

    public void fillDb(View view) {
        List<Penalty> penaltyList = new ArrayList<>();
        penaltyList.add(new Penalty("Persönliches Foul", 15, 0, true, false, ""));
        penaltyList.add(new Penalty("Unsportliches Verhalten Foul", 15, 0, true, false, ""));
    }
}