package ru.softvillage.sms.model.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sim implements Parcelable {

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

    /*@SerializedName("phone_number")
    @Expose*/
    private String phoneNumber;

    public Sim() {
    }

    public Sim(String iccid, String operatorName, int slotNumber, int secureCode) {
        this.iccid = iccid;
        this.operatorName = operatorName;
        this.slotNumber = slotNumber;
        this.secureCode = secureCode;
        phoneNumber = null;
    }

    protected Sim(Parcel in) {
        iccid = in.readString();
        operatorName = in.readString();
        slotNumber = in.readInt();
        secureCode = in.readInt();
    }

    public static final Creator<Sim> CREATOR = new Creator<Sim>() {
        @Override
        public Sim createFromParcel(Parcel in) {
            return new Sim(in.readString(), in.readString(), in.readInt(), in.readInt());
        }

        @Override
        public Sim[] newArray(int size) {
            return new Sim[size];
        }
    };

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
        return "Sim: " + "\r\n" +
                "iccid='" + iccid + '\'' + "\r\n" +
                "operatorName='" + operatorName + '\'' + "\r\n" +
                "slotNumber=" + slotNumber + "\r\n" +
                "secureCode=" + secureCode + "\r\n" +
                "phoneNumber='" + phoneNumber + '\'';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(iccid);
        dest.writeString(operatorName);
        dest.writeInt(slotNumber);
        dest.writeInt(secureCode);
//        dest.writeArray(new String[]{iccid, operatorName, String.valueOf(slotNumber), String.valueOf(secureCode)});

    }
}
