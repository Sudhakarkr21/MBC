package com.example.mbc_tvd.Posting;

import android.os.Handler;
import android.text.TextUtils;

import com.example.mbc_tvd.Adapters.MRLocation_Adapter;
import com.example.mbc_tvd.Adapters.SubDivisionAdapter;
import com.example.mbc_tvd.Others.FunctionCall;
import com.example.mbc_tvd.Others.FunctionGson;
import com.example.mbc_tvd.Values.Billing_Summary;
import com.example.mbc_tvd.Values.GetSetValues;
import com.example.mbc_tvd.Values.TimeStampValues;
import com.google.gson.Gson;
import com.google.gson.JsonArray;


import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import static com.example.mbc_tvd.Others.Constant.BILLED_UNBILLED_FAILURE;
import static com.example.mbc_tvd.Others.Constant.BILLED_UNBILLED_SUCCESS;
import static com.example.mbc_tvd.Others.Constant.BILLING_FILE_SUMMARY_FAILURE;
import static com.example.mbc_tvd.Others.Constant.BILLING_FILE_SUMMARY_SUCCESS;
import static com.example.mbc_tvd.Others.Constant.LOGIN_FAILURE;
import static com.example.mbc_tvd.Others.Constant.LOGIN_SUCCESS;
import static com.example.mbc_tvd.Others.Constant.MRTRACKING_FAILURE;
import static com.example.mbc_tvd.Others.Constant.MRTRACKING_SUCCESS;
import static com.example.mbc_tvd.Others.Constant.SUBDIV_DETAILS_FAILURE;
import static com.example.mbc_tvd.Others.Constant.SUBDIV_DETAILS_SUCCESS;


public class RecevierData {
    FunctionCall functionCall = new FunctionCall();
    FunctionGson functionGson = new FunctionGson();

    public String parseServerXML(String result) {
        String value = "";
        XmlPullParserFactory pullParserFactory;
        InputStream res;
        try {
            res = new ByteArrayInputStream(result.getBytes());
            pullParserFactory = XmlPullParserFactory.newInstance();
            pullParserFactory.setNamespaceAware(true);
            XmlPullParser parser = pullParserFactory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(res, null);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        switch (name) {
                            case "string":
                                value = parser.nextText();
                                break;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public void getMR_Login_status(String result, Handler handler, GetSetValues getSetValues) {
        result = parseServerXML(result);
        functionCall.logStatus("MR Login :" + result);


        try {
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            getSetValues = new Gson().fromJson(jsonObject.toString(), GetSetValues.class);

            if (StringUtils.startsWithIgnoreCase(getSetValues.getMessage(), "Success")) {
                handler.sendEmptyMessage(LOGIN_SUCCESS);
            } else handler.sendEmptyMessage(LOGIN_FAILURE);
        } catch (JSONException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(LOGIN_FAILURE);
        }
    }


    public void get_Details(String result, Handler handler, GetSetValues getSetValues) {
        result = parseServerXML(result);

        try {
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            getSetValues = new Gson().fromJson(jsonObject.toString(), GetSetValues.class);
            if (StringUtils.startsWithIgnoreCase(getSetValues.getMessage(), "Success")) {
                handler.sendEmptyMessage(LOGIN_SUCCESS);
            } else handler.sendEmptyMessage(LOGIN_FAILURE);
        } catch (JSONException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(LOGIN_FAILURE);
        }
    }

    public void getSubdivdetails(String result, android.os.Handler handler, ArrayList<GetSetValues> arrayList, GetSetValues getSetValues, SubDivisionAdapter roleAdapter) {
        result = parseServerXML(result);
//        functionsCall.logStatus("SubDiv_Details" + result);

        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(result);
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    GetSetValues fetch_news = new Gson().fromJson(jsonObject.toString(), GetSetValues.class);
                    arrayList.add(fetch_news);
                    roleAdapter.notifyDataSetChanged();
                }
            }
            if (arrayList.size() > 0) {
                handler.sendEmptyMessage(SUBDIV_DETAILS_SUCCESS);
            } else handler.sendEmptyMessage(SUBDIV_DETAILS_FAILURE);

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    public  void getBilledUnbilledDetails(String result, Handler handler, ArrayList<TimeStampValues> timeStampValues){
        result = parseServerXML(result);

       JSONArray jsonArray;
        try {
            JSONObject jsonObject = new JSONObject(result);
            String message = jsonObject.getString("message");
            if (StringUtils.startsWithIgnoreCase(message, "Failed")) {
                handler.sendEmptyMessage(BILLED_UNBILLED_FAILURE);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        timeStampValues.clear();

        try {
            jsonArray = new JSONArray(result);
            if(jsonArray.length()>0){
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    TimeStampValues timeStampValues1 = new Gson().fromJson(jsonObject.toString(), TimeStampValues.class);
                    timeStampValues.add(timeStampValues1);

                }
            }



            if(timeStampValues.size()>0){
                handler.sendEmptyMessage(BILLED_UNBILLED_SUCCESS);
            }else handler.sendEmptyMessage(BILLED_UNBILLED_FAILURE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getBillingSummary(String result, Handler handler, Billing_Summary billing_summary,ArrayList<Billing_Summary> arrayList){
        result = parseServerXML(result);

        JSONArray jsonArray ;

        try {
            jsonArray = new JSONArray(result);
            if(jsonArray.length()>0) {
                JSONObject jsonobject = jsonArray.getJSONObject(0);
                billing_summary = new Gson().fromJson(jsonobject.toString(), Billing_Summary.class);
                arrayList.add(billing_summary);
            }

            if(!TextUtils.isEmpty(result)){
                handler.sendEmptyMessage(BILLING_FILE_SUMMARY_SUCCESS);
            }else handler.sendEmptyMessage(BILLING_FILE_SUMMARY_FAILURE);

        } catch (JSONException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(BILLING_FILE_SUMMARY_FAILURE);
        }

    }

    public void getMrTracking_Summary(String result, android.os.Handler handler, ArrayList<GetSetValues> arrayList, GetSetValues getSetValues, MRLocation_Adapter adapter) {
        result = parseServerXML(result);
        JSONArray jsonarray;

        try {
            jsonarray = new JSONArray(result);
            for (int i=0;i<jsonarray.length();i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                getSetValues = new Gson().fromJson(jsonObject.toString(), GetSetValues.class);
                arrayList.add(getSetValues);
                adapter.notifyDataSetChanged();
            }

            if(arrayList.size()>0){
                handler.sendEmptyMessage(MRTRACKING_SUCCESS);
            }else handler.sendEmptyMessage(MRTRACKING_FAILURE);
        } catch (JSONException e) {
            handler.sendEmptyMessage(MRTRACKING_FAILURE);
            e.printStackTrace();
        }
    }
}
