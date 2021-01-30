package ru.softvillage.sms;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class MessageSender {
    private final static String TAG = "SVsim";

    private static MessageSender instance;

    private MessageSender() {

    }

    public static void send(Activity activity, String message, int subscriptionId) {

        if (instance == null) {
            instance = new MessageSender();
            while (instance.check(activity)) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        SmsManager simI = SmsManager.getSmsManagerForSubscriptionId(subscriptionId);
        String destinationNumber = "+79281022223";
        ArrayList<String> parts = simI.divideMessage(message);
        simI.sendMultipartTextMessage(destinationNumber, null, parts, null, null);
        Log.i(TAG, "Отправили сообщение на номер " + destinationNumber);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean check(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS},
                    PackageManager.PERMISSION_GRANTED);
            return true;

        }
        return false;
    }
}
