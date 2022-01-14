package com.thomaskorsten.refereeapp;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.thomaskorsten.refereeapp.models.Penalty;

import java.util.List;

public class PenaltyViewModel extends AndroidViewModel {

    private AppRepository appRepository;
    private final List<Penalty> penaltyList;
    private Integer position;
    private Boolean showAnswer;

    public PenaltyViewModel(Application application) {
        super(application);
        appRepository = new AppRepository(application);
        appRepository.reinit();
        penaltyList = appRepository.getPenaltyList();
        position = 0;
        showAnswer = false;
    }

    List<Penalty> getPenaltyList() {
        return penaltyList;
    }
    Integer getPosition() {
        return position;
    }
    Boolean isShowAnswer() {
        return showAnswer;
    }
}
