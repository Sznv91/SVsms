package ru.softvillage.sms.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import ru.softvillage.sms.model.Answer;
import ru.softvillage.sms.model.AuthTo;

public interface CheckNumberApi {

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("/check_number_available.php")
    public Call<Answer> postAuth(@Body AuthTo authTo);
}
