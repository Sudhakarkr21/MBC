package com.example.mbc_tvd.Values;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Billing_Summary implements Serializable {

    @Expose
    @SerializedName("DWNRECORD")
    String DWNRECORD;

    @Expose
    @SerializedName("UPLOADRECORD")
    String UPLOADRECORD;

    @Expose
    @SerializedName("INFOSYSRECORD")
    String INFOSYSRECORD;

    public String getDWNRECORD() {
        return DWNRECORD;
    }

    public void setDWNRECORD(String DWNRECORD) {
        this.DWNRECORD = DWNRECORD;
    }

    public String getUPLOADRECORD() {
        return UPLOADRECORD;
    }

    public void setUPLOADRECORD(String UPLOADRECORD) {
        this.UPLOADRECORD = UPLOADRECORD;
    }

    public String getINFOSYSRECORD() {
        return INFOSYSRECORD;
    }

    public void setINFOSYSRECORD(String INFOSYSRECORD) {
        this.INFOSYSRECORD = INFOSYSRECORD;
    }
}
