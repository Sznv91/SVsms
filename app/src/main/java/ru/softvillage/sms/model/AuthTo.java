package ru.softvillage.sms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AuthTo {

    @SerializedName("data")
    @Expose
    private List<Sim> data;

    @SerializedName("device_id")
    @Expose
    private String deviceId;

    @SerializedName("Android_API")
    @Expose
    private int androidApi;

    public AuthTo(){

    }

    public AuthTo(List<Sim> data, String deviceId, int androidApi) {
        this.data = data;
        this.deviceId = deviceId;
        this.androidApi = androidApi;
    }

    public List<Sim> getData() {
        return data;
    }

    public void setData(List<Sim> data) {
        this.data = data;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getAndroidApi() {
        return androidApi;
    }

    public void setAndroidApi(int androidApi) {
        this.androidApi = androidApi;
    }

    @Override
    public String toString() {
        return "AuthTo{" +
                "data=" + data +
                ", deviceId='" + deviceId + '\'' +
                ", androidApi=" + androidApi +
                '}';
    }
}
