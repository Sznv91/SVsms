package ru.softvillage.sms;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.softvillage.sms.model.Answer;
import ru.softvillage.sms.model.Sim;
import ru.softvillage.sms.model.SimNum;
import ru.softvillage.sms.model.SimNumTo;
import ru.softvillage.sms.network.NetworkService;

public class HttpService extends Service {

    LinearLayout simContent;

    private static final String TAG = "SVsim";

    ExecutorService es;

    MyBinder binder = new MyBinder();



    public HttpService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "MyService onBind");
        return binder;
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG, "MyService onRebind");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Служба запущенна!", Toast.LENGTH_LONG).show();

        es = Executors.newFixedThreadPool(1);
        // Для того чтоб увидить Toast
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        work();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Служба Начинает работать!", Toast.LENGTH_LONG).show();
        ArrayList<Sim> simList = intent.getParcelableArrayListExtra("simList");
//        Sim[] simArray = (Sim[]) intent.getExtras().get("simList");
        Log.d(TAG, "Получили список симкарт в Сервисе" + simList);
//        work(simList);
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void changeTextView(LinearLayout simContent, List<Sim> simList){
        this.simContent = simContent;
        TextView oneSim = this.simContent.findViewWithTag(simList.get(0).getSlotNumber());
        oneSim.setText("ЭТОТ ТЕКСТ НАПИСАН ИЗ РАБОТАЮЩЕГО СЕРВИСА!!!");

        work(simList);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void work(List<Sim> simList) {

        MyRun run = new MyRun(simList);
        es.execute(run);
        //Вызываем по завершении запланированной работы сервиса.
//        stopService(new Intent(this, httpService.class));

        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Служба остановлена", Toast.LENGTH_LONG).show();
    }

    private class MyRun implements Runnable {


        List<Sim> simList;
        Answer[] answer = new Answer[1];

        public MyRun(List<Sim> simList) {
            this.simList = simList;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {

            if (simContent != null){
                TextView oneSim = simContent.findViewWithTag(simList.get(0).getSlotNumber());
                oneSim.setText("ЭТОТ ТЕКСТ ИЗМЕНЕН ИЗ ПОТОКА!!!");
            } else {
                System.out.println("НЕ ИНИЗИАЛИЗИРОВАЛИ simContent");
            }


            //        simList.forEach(sim -> Log.d(TAG, sim.toString() + " Обработка симкарт в work() методе сервиса"));
            Map<String, Boolean> detectNumbers = new HashMap<>();

            List<SimNum> simNumList = new ArrayList<>();
            simNumList.add(new SimNum(simList.get(0).getIccid(), ""));
            // {new Answer(false, "", simNumList)};
            answer[0] = new Answer(false, "", simNumList);

            List<SimNumTo> toSend = new ArrayList<>();
            simList.forEach(sim -> toSend.add(new SimNumTo(sim.getIccid(), "", sim.getSecureCode())));

            boolean flag = true;

            while (flag) {
                Log.i(TAG, "Размер листа с номерами из Answer: " + answer[0].getSimNumList().size());
                Log.i(TAG, "Размер листа с номерами: " + simList.size());

                if (answer[0].getSimNumList().size() == simList.size()) {
                    Log.d(TAG, "Первый этап проверки получения номера телефона.");
//                    Log.d(TAG, "Количество чисел в номере: " + answer[0].getSimNumList().get(0).getNumber().length());
                    for (SimNum sim : answer[0].getSimNumList()) {
                        if (sim.getNumber() != null && sim.getNumber().length() == 11) {
                            Log.d(TAG, "Второй этап проверки получения номера телефона");
                            detectNumbers.putIfAbsent(sim.getIccid(), true);
                        }
                        if (detectNumbers.size() == simList.size()) {
                            Log.d(TAG, "Третий этап проверки получения номера телефона");
                            answer[0].getSimNumList().forEach(answerSim -> Log.d(TAG, answerSim.getNumber() + " Полученный номер телефона"));
                            flag = false;
                            break;
                        }
                    }
                }

                NetworkService.getInstance().postCheckCodeApi().postAuth(toSend).enqueue(new Callback<Answer>() {
                    @Override
                    public void onResponse(Call<Answer> call, Response<Answer> response) {
//                        Log.d(TAG, "Получили ответ от сервера" + answer[0].getSimNumList().get(0).getNumber());
                        answer[0] = response.body();
                    }

                    @Override
                    public void onFailure(Call<Answer> call, Throwable t) {
                        Log.d(TAG, "Сбой отправки " + t.getMessage());
                    }
                });

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    class MyBinder extends Binder {
        HttpService getService() {
            return HttpService.this;
        }
    }

}