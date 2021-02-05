package ru.softvillage.sms.Common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SimEntity {

    @SerializedName("iccid")
    @Expose
    private String iccid;

    @SerializedName("name_o")
    @Expose
    private String operatorName;

    @SerializedName("slot_num")
    @Expose
    private int slotNumber;

    @SerializedName("secure_code")
    @Expose
    private int secureCode;

    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    public int getSecureCode() {
        return secureCode;
    }

    public void setSecureCode(int secureCode) {
        this.secureCode = secureCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "SimEntity{" + "\r\n" +
                "iccid='" + iccid + '\'' + "\r\n" +
                ", operatorName='" + operatorName + '\'' + "\r\n" +
                ", slotNumber=" + slotNumber + "\r\n" +
                ", secureCode=" + secureCode + "\r\n" +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}' + "\r\n";
    }
}
