package ru.softvillage.sms.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import ru.softvillage.sms.model.Answer;
import ru.softvillage.sms.model.SimNumTo;

public interface CheckCodeApi {

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("/check_code_available.php")
    public Call<Answer> postAuth(@Body List<SimNumTo> simNumberList);
}
