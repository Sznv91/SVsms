package ru.softvillage.sms.model;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.softvillage.sms.model.Entity.SimNum;
import ru.softvillage.sms.util.SubscriptionManUtil;
import ru.softvillage.sms.model.Entity.Answer;
import ru.softvillage.sms.model.Entity.AuthTo;
import ru.softvillage.sms.model.Entity.Sim;
import ru.softvillage.sms.network.NetworkService;
import ru.softvillage.sms.presenter.SimPresenter;
import ru.softvillage.sms.util.SecretCodeGenerator;

public class SimCardModel {
    private static SimCardModel instance;

    private static final String TAG = "SVsim";
    private final List<Sim> simList;

    private SimCardModel(SubscriptionManUtil subscriptionList) {
        simList = new ArrayList<>();
        initSims(subscriptionList);
    }

    public static SimCardModel getInstance(SubscriptionManUtil subscriptionList) {
        if (instance == null) {
            instance = new SimCardModel(subscriptionList);
        }
        return instance;
    }

    private void initSims(SubscriptionManUtil subscriptionList) {
        for (SubscriptionInfo subscription : subscriptionList.getList()) {
            simList.add(new Sim(subscription.getIccId(), subscription.getCarrierName().toString(), subscription.getSubscriptionId(), SecretCodeGenerator.getCode()));
        }
    }

    public List<Sim> getSimsList() {
        return simList;
    }

    public void detectSimNumber(SimPresenter presenter) {
        DetectSimNumber detectSimNumber = new DetectSimNumber(presenter);
        detectSimNumber.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public class DetectSimNumber extends AsyncTask<ContentValues, Void, List<Sim>> {
        int flag = 0;
        int smsFlag = 0;
        SimPresenter presenter;

        public DetectSimNumber(SimPresenter presenter) {
            this.presenter = presenter;
        }

        @Override
        protected List<Sim> doInBackground(ContentValues... contentValues) {
            // Узнаем Уникальный идентификатор устройства
            ContentResolver contentResolver = presenter.getView().getContentResolver();
            @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(contentResolver,
                    Settings.Secure.ANDROID_ID);
            Log.d(TAG, "Уникальный идентификатор устройста: " + android_id);
            // Узнаем версию Android API
            int androidVersionApi = android.os.Build.VERSION.SDK_INT;

            // Создаем Transfer Object Authentication
            AuthTo authTo = new AuthTo(simList, android_id, androidVersionApi);
            while (simList.size() > flag) {
                presenter.getNetworkService().postCheckNumberApi().postAuth(authTo).enqueue(new Callback<Answer>() {
                    @Override
                    public void onResponse(Call<Answer> call, Response<Answer> response) {
                        Answer answer = response.body();
                        assert answer != null;
                        for (SimNum num : answer.getSimNumList()) {
                            if (!TextUtils.isEmpty(num.getNumber())) {
                                for (Sim sim : simList) {
                                    if (sim.getIccid().equals(num.getIccid())) {
                                        Log.d(TAG, "Номер телефона " + num.getNumber() + "\r\n flag count = " + flag + "\r\nSimList size: " + simList.size());
                                        sim.setPhoneNumber(num.getNumber());
                                        flag++;
                                    }
                                }
                                //sendSMS();
                                //Добавляем номера телефонов в simList
                            } else {
                                if (smsFlag < 1) {
                                    presenter.sendSms();
                                    smsFlag++;
                                }
                            }
                        }
                        presenter.showPhoneNumberOnDisplay(simList);
                    }

                    @Override
                    public void onFailure(Call<Answer> call, Throwable t) {
                        Log.d(TAG, "Неудачаная доставка по сети. Throwable:\r\n" + t.getMessage());
                    }
                });

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            return simList;
        }

    }


}
