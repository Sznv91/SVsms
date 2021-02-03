package ru.softvillage.sms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Answer {

    @SerializedName("success")
    @Expose
    boolean success;

    @SerializedName("error")
    @Expose
    String error;

    @SerializedName("simList")
    @Expose
    List<SimNum> simNumList;

    public Answer() {

    }

    public Answer(boolean success, String error, List<SimNum> simNumList) {
        this.success = success;
        this.error = error;
        this.simNumList = simNumList;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<SimNum> getSimNumList() {
        return simNumList;
    }

    public void setSimNumList(List<SimNum> simNumList) {
        this.simNumList = simNumList;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "success=" + success +
                ", error='" + error + '\'' +
                ", simNumList=" + simNumList +
                '}';
    }
}
