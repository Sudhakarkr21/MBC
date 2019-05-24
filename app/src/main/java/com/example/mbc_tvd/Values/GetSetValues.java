package com.example.mbc_tvd.Values;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetSetValues implements Serializable {

    String login_role;

    @Expose
    @SerializedName("message")
    String message;
    @Expose
    @SerializedName("MBC_VERSION")
    String MBC_VERSION;
    @Expose
    @SerializedName("SUBDIVCODE")
    String SUBDIVCODE;

    @Expose
    @SerializedName("subdivisionname")
    String subdivisionname;

    String date;

    @Expose
    @SerializedName("MRCODE")
    String MRCODE;

    @Expose
    @SerializedName("MRNAME")
    String MRNAME;

    @Expose
    @SerializedName("MOBILE_NO")
    String MOBILE_NO;

    @Expose
    @SerializedName("DEVICE_ID")
    String DEVICE_ID;

    @Expose
    @SerializedName("LONGITUDE")
    String LONGITUDE;

    @Expose
    @SerializedName("LATITUDE")
    String LATITUDE;

    public String getMRCODE() {
        return MRCODE;
    }

    public void setMRCODE(String MRCODE) {
        this.MRCODE = MRCODE;
    }

    public String getMRNAME() {
        return MRNAME;
    }

    public void setMRNAME(String MRNAME) {
        this.MRNAME = MRNAME;
    }

    public String getMOBILE_NO() {
        return MOBILE_NO;
    }

    public void setMOBILE_NO(String MOBILE_NO) {
        this.MOBILE_NO = MOBILE_NO;
    }

    public String getDEVICE_ID() {
        return DEVICE_ID;
    }

    public void setDEVICE_ID(String DEVICE_ID) {
        this.DEVICE_ID = DEVICE_ID;
    }

    public String getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public List<TimeStampValues> timeStampValues;

    public List<TimeStampValues> getTimeStampValues() {
        return timeStampValues;
    }

    public void setTimeStampValues(List<TimeStampValues> timeStampValues) {
        this.timeStampValues = timeStampValues;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getSubdivisionname() {
        return subdivisionname;
    }

    public void setSubdivisionname(String subdivisionname) {
        this.subdivisionname = subdivisionname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLogin_role() {
        return login_role;
    }

    public String getMBC_VERSION() {
        return MBC_VERSION;
    }

    public void setMBC_VERSION(String MBC_VERSION) {
        this.MBC_VERSION = MBC_VERSION;
    }

    public String getSUBDIVCODE() {
        return SUBDIVCODE;
    }

    public void setSUBDIVCODE(String SUBDIVCODE) {
        this.SUBDIVCODE = SUBDIVCODE;
    }

    public void setLogin_role(String login_role) {
        this.login_role = login_role;
    }
}
