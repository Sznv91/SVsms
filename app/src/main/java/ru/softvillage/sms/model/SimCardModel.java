package ru.softvillage.sms.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.softvillage.sms.SubscriptionManUtil;
import ru.softvillage.sms.model.Entity.Answer;
import ru.softvillage.sms.model.Entity.AuthTo;
import ru.softvillage.sms.model.Entity.Sim;
import ru.softvillage.sms.model.Entity.SimNum;
import ru.softvillage.sms.network.NetworkService;
import ru.softvillage.sms.util.SecretCodeGenerator;

public class SimCardModel {
    private static final String TAG = "SVsim";


    private final List<Sim> simList;
    Activity view;

    private final NetworkService networkService;

    public SimCardModel(NetworkService networkService, Activity view) {
        this.networkService = networkService;
        this.view = view;
        simList = new ArrayList<>();
        initSims();
    }

    private void initSims() {
        SubscriptionManUtil subscriptionList = new SubscriptionManUtil(view); // Переделать на static
        for (SubscriptionInfo subscription : subscriptionList.getList()) {
            simList.add(new Sim(subscription.getIccId(), subscription.getCarrierName().toString(), subscription.getSubscriptionId(), SecretCodeGenerator.getCode()));
        }
    }

    public List<Sim> getSimsList(CompleteDetectCallback callback) {
        DetectSimNumber detectSimNumber = new DetectSimNumber(callback);
        detectSimNumber.execute();
        return simList;
    }

    public interface CompleteDetectCallback {
        void onDetect(List<Sim> sims);
    }

    class DetectSimNumber extends AsyncTask<ContentValues, Void, List<Sim>> {

        private final CompleteDetectCallback callback;

        public DetectSimNumber(CompleteDetectCallback callback) {
            this.callback = callback;
        }

        @Override
        protected void onPostExecute(List<Sim> sims) {
            if (callback != null) {
                Log.d(TAG, "DetectSimNumber -> onPostExecute");
                callback.onDetect(sims);
//                Log.d(TAG, "Возвращаем callback");

            }
//            super.onPostExecute(sims);
        }

        @Override
        protected List<Sim> doInBackground(ContentValues... contentValues) {
            Log.d(TAG, "DetectSimNumber -> doInBackground");
            // Узнаем Уникальный идентификатор устройства
            ContentResolver contentResolver = view.getContentResolver();
            @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(contentResolver,
                    Settings.Secure.ANDROID_ID);
            // Узнаем версию Android API
            int androidVersionApi = android.os.Build.VERSION.SDK_INT;

            // Создаем Transfer Object Authentication
            AuthTo authTo = new AuthTo(simList, android_id, androidVersionApi);

            networkService.postCheckNumberApi().postAuth(authTo).enqueue(new Callback<Answer>() {
                @Override
                public void onResponse(Call<Answer> call, Response<Answer> response) {
                    Log.d(TAG, "Получили ответ от сервера: " + response.body());
                    Answer answer = response.body();
                    for (SimNum num : answer.getSimNumList()) {
                        if (!TextUtils.isEmpty(num.getNumber())) {
                            Log.d(TAG, "Получили ответ от сервера; number: " + num.getNumber());

                            //sendSMS();
                            //Добавляем номера телефонов в simList
                        }
                    }
                }

                @Override
                public void onFailure(Call<Answer> call, Throwable t) {
                    Log.d(TAG, "Передача не удалась");
                }
            });


            return simList;
        }

    }


}
