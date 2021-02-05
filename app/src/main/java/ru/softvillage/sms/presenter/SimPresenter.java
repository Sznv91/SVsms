package ru.softvillage.sms.presenter;

import android.app.Activity;

import java.util.List;

import ru.softvillage.sms.Common.SimEntity;
import ru.softvillage.sms.modelMVP.SimModel;
import ru.softvillage.sms.view.SimActivity;

public class SimPresenter {

    private SimActivity view;
    private final SimModel model;

    public SimPresenter(SimModel model) {
        this.model = model;
    }

    public void attachActivity(SimActivity activity) {
        view = activity;
    }

    public void loadSims() {
        model.loadSims(sims -> view.showSims(sims));
    }
}
