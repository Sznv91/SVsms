package ru.softvillage.sms.network;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static final String TAG = "SVsim";

    private static NetworkService mInstance;
    private static final String BASE_URL = "http://kkt-evotor.ru"; //https
    private final Retrofit mRetrofit;

    private NetworkService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public static NetworkService getInstance() {
//        Log.d(TAG, "Получаем экземпляр класса NetworkService");
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public CheckNumberApi postCheckNumberApi() {
        return mRetrofit.create(CheckNumberApi.class);
    }

    public CheckCodeApi postCheckCodeApi() {
        return mRetrofit.create(CheckCodeApi.class);
    }

}
