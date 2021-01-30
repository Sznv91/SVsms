package ru.softvillage.sms;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.getSystemService;

public class SubscriptionManUtil {

    SubscriptionManager subscriptionManager;

    /**
     * Запрашиваем разрешение на чтение информации о симкартах.
     */
    private void init(Activity activity) {
        subscriptionManager = (SubscriptionManager) activity.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE},
                    PackageManager.PERMISSION_GRANTED);
            init(activity);
        }
    }

    public List<SubscriptionInfo> getList(Activity activity) {
        if (subscriptionManager == null) {
            init(activity);
            return new ArrayList<>(subscriptionManager.getActiveSubscriptionInfoList());
        }
        return new ArrayList<>(subscriptionManager.getActiveSubscriptionInfoList());
    }
}
