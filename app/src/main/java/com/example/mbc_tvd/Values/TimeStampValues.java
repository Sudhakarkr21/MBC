package com.example.mbc_tvd.Values;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TimeStampValues implements Serializable {

    @Expose
    @SerializedName("MR_Code")
    String MRCode;

    @Expose
    @SerializedName("Bipcount")
    String Bipcount;

    @Expose
    @SerializedName("Download_Record")
    String Download_Record;

    @Expose
    @SerializedName("Download_DateTime")
    String Download_DateTime;

    @Expose
    @SerializedName("Billed_Record")
    String Billed_Record;

    @Expose
    @SerializedName("Billed_DateTime")
    String Billed_DateTime;

    @Expose
    @SerializedName("Ftp_Status")
    String Ftp_Status;

    @Expose
    @SerializedName("Status")
    String Status;

    String UnBilled_Record;

    public String getUnBilled_Record() {
        return UnBilled_Record;
    }

    public void setUnBilled_Record(String unBilled_Record) {
        UnBilled_Record = unBilled_Record;
    }

    public String getMRCode() {
        return MRCode;
    }

    public void setMRCode(String MRCode) {
        this.MRCode = MRCode;
    }

    public String getBipcount() {
        return Bipcount;
    }

    public void setBipcount(String bipcount) {
        Bipcount = bipcount;
    }

    public String getDownload_Record() {
        return Download_Record;
    }

    public void setDownload_Record(String download_Record) {
        Download_Record = download_Record;
    }

    public String getDownload_DateTime() {
        return Download_DateTime;
    }

    public void setDownload_DateTime(String download_DateTime) {
        Download_DateTime = download_DateTime;
    }

    public String getBilled_Record() {
        return Billed_Record;
    }

    public void setBilled_Record(String billed_Record) {
        Billed_Record = billed_Record;
    }

    public String getBilled_DateTime() {
        return Billed_DateTime;
    }

    public void setBilled_DateTime(String billed_DateTime) {
        Billed_DateTime = billed_DateTime;
    }

    public String getFtp_Status() {
        return Ftp_Status;
    }

    public void setFtp_Status(String ftp_Status) {
        Ftp_Status = ftp_Status;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
