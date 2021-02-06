package ru.softvillage.sms.model.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SimNumTo extends SimNum {

    @SerializedName("code")
    @Expose
    private int secretCode;

    public SimNumTo(String iccid, String number, int secretCode) {
        super(iccid, number);
        this.secretCode = secretCode;
    }

    public SimNumTo() {

    }

    public int getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(int secretCode) {
        this.secretCode = secretCode;
    }

    @Override
    public String toString() {
        return "SimNumTo{" +
                "secretCode=" + secretCode +
                ", iccid='" + iccid + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
