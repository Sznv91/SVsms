package ru.softvillage.sms.util;

import android.util.Log;

public class SecretCodeGenerator {

    private static final String TAG = "SVsim";

    public static int getCode() {
        int min = 10000;
        int max = 99999;

        max -= min;
        int result = (int) (Math.random() * ++max) + min;
        Log.i(TAG, result + " Сгенерировали секретный код для отправки на СМС Шлюз");
        return result;
    }
}
