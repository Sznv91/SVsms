package ru.softvillage.sms.presenter;

import android.util.Log;

import java.util.List;

import ru.softvillage.sms.model.Entity.Sim;
import ru.softvillage.sms.model.SimCardModel;
import ru.softvillage.sms.network.NetworkService;
import ru.softvillage.sms.view.SimActivity;

public class SimPresenter {
    private static final String TAG = "SVsim";


    SimActivity view;
    SimCardModel model;

    public SimPresenter(SimActivity view) {
        this.view = view;
        model = new SimCardModel(NetworkService.getInstance(), this.view);
        init();
    }

    private void init() {
//        model = new SimCardModel(NetworkService.getInstance(), view);
        List<Sim> sims = model.getSimsList(new SimCardModel.CompleteDetectCallback() {
            @Override
            public void onDetect(List<Sim> sims) {
                Log.d(TAG, "SimPresenter -> init -> onDetect");
//                view.showSims(sims);
            }
        });
        for (Sim sim : sims) {
            Log.d(TAG, sim.toString() + "\r\nSimPresenter -> init -> foreach");
        }


        view.initSims(model.getSimsList(new SimCardModel.CompleteDetectCallback() {
            @Override
            public void onDetect(List<Sim> sims) {
                Log.d(TAG, "SimPresenter -> init -> onDetect");
//                view.showSims(sims);
            }
        }));
        view.showStatus("Выполнили инициализацию сим карт");

//        model.
    }


}
