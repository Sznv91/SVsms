package ru.softvillage.sms.presenter;

import android.util.Log;

import java.util.List;

import ru.softvillage.sms.util.MessageSender;
import ru.softvillage.sms.util.SubscriptionManUtil;
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
        model = new SimCardModel(new SubscriptionManUtil(view));
        init();
    }

    private void init() {
        view.initSims(model.getSimsList(/*this*/));
        view.showStatus("Выполнили инициализацию сим карт");
        model.detectSimNumber(this);
    }

    public void showPhoneNumberOnDisplay(List<Sim> simList) {
        view.showSims(simList);
        view.showStatus("Определили номера телефонов");
    }

    public void sendSms() {
        for (Sim sim : model.getSimsList()) {
            MessageSender.send(view, String.valueOf(sim.getSecureCode()), sim.getSlotNumber());
            Log.i(TAG, "Отправили на шлюз код " + String.valueOf(sim.getSecureCode()) + " c sim №: " + sim.getSlotNumber());
        }
    }

    public SimActivity getView() {
        return view;
    }

    public NetworkService getNetworkService() {
        return NetworkService.getInstance();
    }


}
