package ru.softvillage.sms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.softvillage.sms.model.Answer;
import ru.softvillage.sms.model.AuthTo;
import ru.softvillage.sms.model.Sim;
import ru.softvillage.sms.model.SimNum;
import ru.softvillage.sms.network.NetworkService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "SVsim";

    Boolean bound = false;

    HttpService myService;


    SubscriptionManUtil subscriptions = new SubscriptionManUtil();
    private List<SimNum> telephoneNumbers;
    private ArrayList<Sim> simList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//  Выводим на экран информацю о сим.

        // init SIM list
        makeSimList(subscriptions.getList(this));
        TextView simCount = findViewById(R.id.sim_count);
        Log.i(TAG, simList.size() + " Размер списка активных симкарт");
        simCount.setText(String.valueOf(simList.size()));

        LinearLayout simContent = findViewById(R.id.sim_content);
        for (Sim sim : simList) {
            String result = "";
            result += sim.getOperatorName() + "\r\n";
            result += sim.getIccid() + "\r\n";
            result += "Slot: " + sim.getSlotNumber() + "\r\n";
            result += "Tel №:" + "\r\n";
            result += "SecureCode: " + sim.getSecureCode();
            Log.i(TAG, result + " Сформировали текст с доступной информацией. Sim №: " + sim.getSlotNumber());
            TextView simInfo = new TextView(this);
            simInfo.setId(sim.getSlotNumber());
            simInfo.setText(result);
            simInfo.setPadding(20, 20, 20, 20);
            simContent.addView(simInfo);
        }

        //Запуск http службы
        Intent intent = new Intent(MainActivity.this, HttpService.class);
        intent.putParcelableArrayListExtra("simList", simList);

        startService(intent);

        // Отправляем по HTTP иформацию о симкартах и Проверочном коде.
        getAuthHttp();

        // Биндим Сервис
        //https://startandroid.ru/ru/uroki/vse-uroki-spiskom/162-urok-97-service-binding-serviceconnection.html
        ServiceConnection sConn = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder binder) {
                Log.d(TAG, "MainActivity onServiceConnected");
                    myService = ((HttpService.MyBinder) binder).getService();
                bound = true;
            }

            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "MainActivity onServiceDisconnected");
                bound = false;
            }
        };

        bindService(intent, sConn, BIND_AUTO_CREATE);

//        myService.binder
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putString("Secret", secretCode);
    }

    private int generateSecretCode() {
        int min = 10000;
        int max = 99999;

        max -= min;
        int result = (int) (Math.random() * ++max) + min;
        Log.i(TAG, result + " Сгенерировали секретный код для отправки на СМС Шлюз");
        return result;
    }

    /**
     * Отправляем сгенерированный код на СМС шлюз Soft Village
     * и на Application Controller.
     * Затем начинаем опрашивать сервер о наличае данных о устройстве.
     */
    public void fillTelNo(View view) {
        //SMS
        for (Sim sim : simList) {
            MessageSender.send(this, String.valueOf(sim.getSecureCode()), sim.getSlotNumber());
            Log.i(TAG, "Отправили на шлюз код " + String.valueOf(sim.getSecureCode()) + " c sim №: " + sim.getSlotNumber());
        }
        //HTTP
        stopService(new Intent(MainActivity.this, HttpService.class));

    }

    public void exit(View view) throws InterruptedException {
        System.exit(1);
    }

    /**
     * Формируем список симкарт с информацией о них. Для отправки по HTTP и отображения в MainActivity
     */
    private void makeSimList(List<SubscriptionInfo> subscriptionList) {
        simList = new ArrayList<>();
        for (SubscriptionInfo sim : subscriptionList) {
            int secretCode = generateSecretCode();
            simList.add(new Sim(sim.getIccId(), sim.getCarrierName().toString(), sim.getSubscriptionId(), secretCode));
        }
    }

    /**
     * Необходимо перенести в сервисы
     */
    private void getAuthHttp() {
        Log.i(TAG, "Подготавливаем AuthTo для отправки на сервер.");

        // Узнаем Уникальный идентификатор устройства
        ContentResolver contentResolver = getContentResolver();
        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(/*getContext().getContentResolver()*/ contentResolver,
                Settings.Secure.ANDROID_ID);

        // Узнаем версию Android API
        int androidVersionApi = android.os.Build.VERSION.SDK_INT;

        // Создаем Transfer Object Authentication
        AuthTo authTo = new AuthTo(simList, android_id, androidVersionApi);

        Log.i(TAG, "Сформировали обьект authTo " + authTo.toString());

        NetworkService.getInstance().postCheckNumberApi().postAuth(authTo).enqueue(new Callback<Answer>() {

            @Override
            public void onResponse(Call<Answer> call, Response<Answer> response) {
                Log.d(TAG, "Success " + response.body().toString());
                Answer answer = response.body();
                telephoneNumbers = new ArrayList<>(answer.getSimNumList());
                Log.i(TAG, "Получили номера телефонов " + telephoneNumbers);
            }

            @Override
            public void onFailure(Call<Answer> call, Throwable t) {
                Log.d(TAG, "UnSuccess " + t.getMessage());

            }
        });

        Log.i(TAG, "Ожидаем что отправка уже выполнена.");
    }

}