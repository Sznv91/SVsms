package ru.softvillage.sms;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.softvillage.sms.model.Entity.Answer;
import ru.softvillage.sms.model.Entity.Sim;
import ru.softvillage.sms.model.Entity.SimNum;
import ru.softvillage.sms.model.Entity.SimNumTo;
import ru.softvillage.sms.network.NetworkService;

public class HttpService extends Service {

    Activity activity;

    LinearLayout simContent;

    private static final String TAG = "SVsim";

    ExecutorService es;

    MyBinder binder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "MyService onBind");
        return binder;
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
//        work();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Служба Начинает работать!", Toast.LENGTH_LONG).show();
        ArrayList<Sim> simList = intent.getParcelableArrayListExtra("simList");
        Log.d(TAG, "Получили список симкарт в Сервисе" + simList);
//        work(simList);
        return super.onStartCommand(intent, flags, startId);
    }

    public void changeTextView(Activity act, LinearLayout simContent, List<Sim> simList) {
        activity = act;
        this.simContent = simContent;
        /*TextView oneSim = this.simContent.findViewWithTag(simList.get(0).getIccid()); //Для демонстрации изменения текста из сервиса
        oneSim.setText("ЭТОТ ТЕКСТ НАПИСАН ИЗ РАБОТАЮЩЕГО СЕРВИСА!!!");*/               //Для демонстрации изменения текста из сервиса

        work(simList);
        stopSelf();
    }

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
        Answer answer;

        public MyRun(List<Sim> simList) {
            this.simList = simList;
        }

        @Override
        public void run() {

            /*if (simContent != null){
                TextView oneSim = simContent.findViewWithTag(simList.get(0).getSlotNumber());
                oneSim.setText("ЭТОТ ТЕКСТ ИЗМЕНЕН ИЗ ПОТОКА!!!");

            } else {
                System.out.println("НЕ ИНИЦИАЛИЗИРОВАЛИ simContent");
            }*/

            Map<String, Boolean> detectNumbers = new HashMap<>(); //Возможно надо заменить на counter int

            List<SimNum> simNumList = new ArrayList<>();
            simNumList.add(new SimNum(simList.get(0).getIccid(), "")); // Необходим для первичного прохода цикла while
            answer = new Answer(false, "", simNumList);          // Необходим для первичного прохода цикла while

            List<SimNumTo> toSend = new ArrayList<>();
            for (Sim sim : simList) {
                toSend.add(new SimNumTo(sim.getIccid(), "", sim.getSecureCode()));
            }

            boolean flag = true;

            while (flag) {
                Log.i(TAG, "Размер листа с номерами из Answer: " + answer.getSimNumList().size());
                Log.i(TAG, "Размер листа с номерами: " + simList.size());

                if (answer.getSimNumList().size() == simList.size()) {
                    Log.d(TAG, "Первый этап проверки получения номера телефона.");
                    for (SimNum sim : answer.getSimNumList()) {
                        if (sim.getNumber() != null && sim.getNumber().length() == 11) {
                            Log.d(TAG, "Второй этап проверки получения номера телефона");

                            detectNumbers.put(sim.getIccid(), true);
                        }
                        if (detectNumbers.size() == simList.size()) {
                            Log.d(TAG, "Третий этап проверки получения номера телефона");
                            performOnScreenDisplay(answer);
                            flag = false;
                            break;
                        }
                    }
                }

                NetworkService.getInstance().postCheckCodeApi().postAuth(toSend).enqueue(new Callback<Answer>() {
                    @Override
                    public void onResponse(Call<Answer> call, Response<Answer> response) {
//                        Log.d(TAG, "Получили ответ от сервера" + answer[0].getSimNumList().get(0).getNumber());
                        answer = response.body();
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

        private void performOnScreenDisplay(@NotNull Answer answer) {

            activity.runOnUiThread(() -> {
                for (SimNum sim : answer.getSimNumList()) {
                    Log.d(TAG, sim.getNumber() + " Полученный номер телефона");
                    TextView onDisplay = simContent.findViewWithTag(sim.getIccid());
                    String text = onDisplay.getText().toString() + "\r\nTel №: " + sim.getNumber();
                    onDisplay.setText(text);
                }
            });
        }
    }

    class MyBinder extends Binder {
        HttpService getService() {
            return HttpService.this;
        }
    }

}