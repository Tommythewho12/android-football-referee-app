package com.thomaskorsten.refereeapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.thomaskorsten.refereeapp.models.Penalty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PenaltiesActivity extends AppCompatActivity {
    private static final int DEFAULT_COLOR = Color.BLACK;
    private static final int CORRECT_COLOR = Color.GREEN;
    private static final int INCORRECT_COLOR = Color.RED;
    private static final int DEFAULT_BUTTON_COLOR = Color.BLACK;
    private static final int CORRECT_BUTTON_COLOR = CORRECT_COLOR;
    private static final int INCORRECT_BUTTON_COLOR = INCORRECT_COLOR;

    private PenaltyViewModel penaltyViewModel;
    private Integer position;
    private List<Penalty> penaltyList;
    private Boolean showAnswer;
    TextView tv_foul;
    RadioButton rb_yards0, rb_yards5, rb_yards10, rb_yards15, rb_es_basic, rb_es_prev, rb_es_succ, rb_es_foul;
    CheckBox cb_autoFirst, cb_lossOfDown;
    Button btn_next;

    private List<CompoundButton> resetList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penalties_question);

        penaltyViewModel = new ViewModelProvider(this).get(PenaltyViewModel.class);
        penaltyList = penaltyViewModel.getPenaltyList();
        position = penaltyViewModel.getPosition();
        showAnswer = penaltyViewModel.isShowAnswer();

        tv_foul = findViewById(R.id.penalties_penalty_name);

        rb_yards0 = findViewById(R.id.penalties_penalty_rb_yards0);
        rb_yards5 = findViewById(R.id.penalties_penalty_rb_yards5);
        rb_yards10 = findViewById(R.id.penalties_penalty_rb_yards10);
        rb_yards15 = findViewById(R.id.penalties_penalty_rb_yards15);
        rb_es_basic = findViewById(R.id.penalties_penalty_rb_enforcementSpotBasic);
        rb_es_foul = findViewById(R.id.penalties_penalty_rb_enforcementSpotFoul);
        rb_es_prev = findViewById(R.id.penalties_penalty_rb_enforcementSpotPrevious);
        rb_es_succ = findViewById(R.id.penalties_penalty_rb_enforcementSpotSucceeding);
        cb_autoFirst = findViewById(R.id.penalties_penalty_cb_autoFirst);
        cb_lossOfDown = findViewById(R.id.penalties_penalty_cb_lossOfDown);
        resetList.add(rb_yards0);
        resetList.add(rb_yards5);
        resetList.add(rb_yards10);
        resetList.add(rb_yards15);
        resetList.add(rb_es_basic);
        resetList.add(rb_es_foul);
        resetList.add(rb_es_prev);
        resetList.add(rb_es_succ);
        resetList.add(cb_autoFirst);
        resetList.add(cb_lossOfDown);

        btn_next = findViewById(R.id.penalties_penalty_button);

        renderView();
    }

    private void renderView() {
        if (position >= penaltyList.size()) {
            endQuiz();
        } else {
            Penalty temp = penaltyList.get(position);
            tv_foul.setText(temp.name);
        }

    }

    private void nextPenalty() {
        for (CompoundButton cb : resetList) {
            cb.setTextColor(DEFAULT_COLOR);
            cb.setChecked(false);
            cb.setEnabled(true);
        }
        btn_next.setBackgroundColor(DEFAULT_BUTTON_COLOR);
        position++;
        renderView();
    }

    private void revealAnswer() {
        Penalty temp = penaltyList.get(position);
        for (CompoundButton cb : resetList) {
            cb.setEnabled(false);
        }
        boolean allCorrect = true;

        for (int i=0; i<4; i++) {
            // TODO:  go through question
        }
        switch(temp.yards) {
            case 0:
                rb_yards0.setTextColor(CORRECT_COLOR);
                break;
            case 5:
                rb_yards5.setTextColor(CORRECT_COLOR);
                break;
            case 10:
                rb_yards10.setTextColor(CORRECT_COLOR);
                break;
            case 15:
                rb_yards15.setTextColor(CORRECT_COLOR);
                break;
        }
        switch(temp.spot) {
            // 0 = basic, 1 = previous, 2 = succeeding, 3 = spot of foul
            case 0:
                rb_es_basic.setTextColor(CORRECT_COLOR);
                break;
            case 1:
                rb_es_prev.setTextColor(CORRECT_COLOR);
                break;
            case 2:
                rb_es_succ.setTextColor(CORRECT_COLOR);
                break;
            case 3:
                rb_es_foul.setTextColor(CORRECT_COLOR);
                break;
        }
        if (temp.automaticFirstDown == cb_autoFirst.isChecked()) {
            cb_autoFirst.setTextColor(CORRECT_COLOR);
        } else {
            cb_autoFirst.setTextColor(INCORRECT_COLOR);
            allCorrect = false;
        }
        if (temp.lossOfDown == cb_lossOfDown.isChecked()) {
            cb_lossOfDown.setTextColor(CORRECT_COLOR);
        } else {
            cb_lossOfDown.setTextColor(INCORRECT_COLOR);
            allCorrect = false;
        }

        if (allCorrect) {
            btn_next.setBackgroundColor(CORRECT_BUTTON_COLOR);
        } else {
            btn_next.setBackgroundColor(INCORRECT_BUTTON_COLOR);
        }
    }

    private void endQuiz() {

    }

    public void nextButtonPressed(View view) {
        if (showAnswer) {
            nextPenalty();
        } else {
            revealAnswer();
        }
        showAnswer = !showAnswer;
    }
}