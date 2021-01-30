package ru.softvillage.sms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "SVsim";

    List<SubscriptionInfo> subscriptionList;
    SubscriptionManUtil subscriptions = new SubscriptionManUtil();
    String secretCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// Формируем проверочный код
        if (savedInstanceState != null) {
            secretCode = savedInstanceState.getString("Secret", null);
            if (secretCode == null) {
                secretCode = generateSecretCode();
            }
        } else {
            secretCode = generateSecretCode();
        }
        TextView secretCodeView = findViewById(R.id.code_dynamic);
        secretCodeView.setText(secretCode);
//  Выводим на экран информацю о сим.
        subscriptionList = subscriptions.getList(this);
        TextView simCount = findViewById(R.id.sim_count);
        Log.i(TAG, subscriptionList.size() + " Размер списка активных симкарт");
        simCount.setText(String.valueOf(subscriptionList.size()));

        LinearLayout simContent = findViewById(R.id.sim_content);
        for (SubscriptionInfo sim : subscriptionList) {
            String result = "";
            result += sim.getCarrierName() + "\r\n";
            result += sim.getIccId() + "\r\n";
            result += "Slot: " + sim.getSubscriptionId() + "\r\n";
            result += "Tel №:";
            Log.i(TAG, result + " Сформировали текст с доступной информацией. Sim №: " + sim.getSubscriptionId());
            TextView simInfo = new TextView(this);
            simInfo.setId(sim.getSubscriptionId());
            simInfo.setText(result);
            simInfo.setPadding(20, 20, 20, 20);
            simContent.addView(simInfo);
        }


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Secret", secretCode);
    }

    private String generateSecretCode() {
        int min = 10000;
        int max = 99999;

        max -= min;
        int result = (int) (Math.random() * ++max) + min;
        Log.i(TAG, result + " Сгенерировали секретный код для отправки на СМС Шлюз");
        return String.valueOf(result);
    }

    /**
     * Отправляем сгенерированный код на СМС шлюз Soft Village
     * и на Application Controller.
     * Затем начинаем опрашивать сервер о наличае данных о устройстве.
     */
    public void fillTelNo(View view) {
        for (SubscriptionInfo sim : subscriptionList) {
            secretCode = generateSecretCode();
            MessageSender.send(this, secretCode, sim.getSubscriptionId());
            Log.i(TAG, "Отправили на шлюз код " + secretCode + " c sim №: " + sim.getSubscriptionId());
        }
    }

    public void exit(View view) {
        System.exit(1);
    }
}