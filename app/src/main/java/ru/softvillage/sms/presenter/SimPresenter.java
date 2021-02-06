package ru.softvillage.sms.presenter;

import java.util.List;

import ru.softvillage.sms.model.Entity.Sim;
import ru.softvillage.sms.model.SimCardModel;
import ru.softvillage.sms.network.NetworkService;
import ru.softvillage.sms.view.SimActivity;

public class SimPresenter {

    SimActivity view;
    SimCardModel model;

    public SimPresenter(SimActivity view) {
        this.view = view;
        init();
    }

    private void init() {
        model = new SimCardModel(NetworkService.getInstance(), view);
        view.initSims(model.getSimsList(new SimCardModel.CompleteDetectCallback() {
            @Override
            public void onDetect(List<Sim> sims) {
                view.showSims(sims);
            }
        }));
        view.showStatus("Выполнили инициализацию сим карт");

//        model.
    }


}
