package ru.softvillage.sms;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class HttpService extends Service {
    public HttpService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this,"Служба запущенна!",Toast.LENGTH_LONG).show();

        work();

    }

    private void work(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Вызываем по завершении запланированной работы сервиса.
//        stopService(new Intent(this, httpService.class));

        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Служба остановлена", Toast.LENGTH_LONG).show();
    }
}