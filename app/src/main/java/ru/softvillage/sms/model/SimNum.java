package ru.softvillage.sms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SimNum {

    @SerializedName("iccid")
    @Expose
    String iccid;

    @SerializedName("number")
    @Expose
    String number;

    public SimNum() {

    }

    public SimNum(String iccid, String number) {
        this.iccid = iccid;
        this.number = number;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "SimNum{" +
                "iccid='" + iccid + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
