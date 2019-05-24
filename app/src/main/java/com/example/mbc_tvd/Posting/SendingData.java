package com.example.mbc_tvd.Posting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.example.mbc_tvd.Adapters.MRLocation_Adapter;
import com.example.mbc_tvd.Adapters.SubDivisionAdapter;
import com.example.mbc_tvd.Others.FunctionCall;
import com.example.mbc_tvd.Values.Billing_Summary;
import com.example.mbc_tvd.Values.GetSetValues;
import com.example.mbc_tvd.Values.TimeStampValues;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static android.content.Context.MODE_PRIVATE;
import static com.example.mbc_tvd.Others.Constant.BILLING_SERVICE;
import static com.example.mbc_tvd.Others.Constant.COLLECTION_SERVICE;
import static com.example.mbc_tvd.Others.Constant.REAL_TRM_URL;
import static com.example.mbc_tvd.Others.Constant.REAL_TRM_URL2;
import static com.example.mbc_tvd.Others.Constant.REAL_TRM_URL3;
import static com.example.mbc_tvd.Others.Constant.SERVICE;
import static com.example.mbc_tvd.Others.Constant.SERVICE2;
import static com.example.mbc_tvd.Others.Constant.TEST_TRM_URL;
import static com.example.mbc_tvd.Others.Constant.TEST_TRM_URL3;
import static com.example.mbc_tvd.Others.Constant.TRM_TEST_URL2;
import static com.example.mbc_tvd.Others.Constant.TRM_URL;

public class SendingData {
    RecevierData recevierData = new RecevierData();
    FunctionCall functionCall = new FunctionCall();
    private String BASEURL,BASE_BILLING_URL,BASE_URL_BILLEDUNBILLED;
    private String BASE_TICKETING_LOGIN,BASE_COLLECTION_URL;
    public SendingData(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        String test_real = sharedPreferences.getString("TEST_REAL_SERVER", "");
        if (StringUtils.equalsIgnoreCase(test_real,"TEST"))
            server_link(0);
            //flag = "test";
        else
            server_link(1);
        //flag = "real";
    }

    private void server_link(int val)
    {
        if (val == 0)
        {
            BASE_COLLECTION_URL = TRM_TEST_URL2 + COLLECTION_SERVICE;
            BASE_BILLING_URL = TRM_TEST_URL2 + BILLING_SERVICE;
            BASEURL = TEST_TRM_URL + SERVICE;
            BASE_TICKETING_LOGIN = TEST_TRM_URL + SERVICE2;
            BASE_URL_BILLEDUNBILLED = TEST_TRM_URL3 + SERVICE;

        }else {
            BASE_COLLECTION_URL = REAL_TRM_URL2 + COLLECTION_SERVICE;
            BASE_BILLING_URL = TRM_URL + BILLING_SERVICE;
            BASEURL = REAL_TRM_URL + SERVICE;
            BASE_TICKETING_LOGIN = REAL_TRM_URL + SERVICE2;
            BASE_URL_BILLEDUNBILLED = REAL_TRM_URL3 + SERVICE;
        }
    }

    private String UrlPostConnection(String Post_Url, HashMap<String, String> datamap) throws IOException {
        try {
            StringBuilder response = new StringBuilder();
            URL url = new URL(Post_Url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(60000);
            conn.setConnectTimeout(60000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(datamap));
            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            } else {
                response = new StringBuilder();
            }
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Debug", "SERVER TIME OUT");

        }
        return null;
    }


    private String UrlPostConnection(String Post_Url) throws IOException {
        String response = "";
        URL url = new URL(Post_Url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream outputStream = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        outputStream.close();
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = bufferedReader.readLine()) != null) {
                response += line;
            }
        } else {
            response = "";
        }
        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }


    @SuppressLint("StaticFieldLeak")
    public class Login extends AsyncTask<String, String, String> {
        String response = "";
        GetSetValues simpleValues;
        Handler handler;
        public Login(GetSetValues simpleValues, Handler handler) {
            this.simpleValues = simpleValues;
            this.handler = handler;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("username", params[0]);
            datamap.put("password", params[1]);
            try {
                response = UrlPostConnection(BASE_TICKETING_LOGIN + "loginDetails", datamap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            recevierData.get_Details(result, handler, simpleValues);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class MR_Login extends AsyncTask<String, String, String> {
        String response="", mrcode="";
        Handler handler;
        GetSetValues getSetValues;
        public MR_Login(Handler handler, GetSetValues getSetValues) {
            this.handler = handler;
            this.getSetValues = getSetValues;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("MRCode", params[0]);
            datamap.put("DeviceId", params[1]);
            datamap.put("PASSWORD", params[2]);
            datamap.put("Date","");
            functionCall.logStatus("MRCode: "+mrcode + "\n" + "DeviceID: "+params[1] + "\n" + "Password: "+params[2]);
            try {
                response = UrlPostConnection(BASE_COLLECTION_URL+"MRDetails", datamap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            recevierData.getMR_Login_status(result, handler, getSetValues);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class SendSubdivCodeRequest extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues> arrayList;
        SubDivisionAdapter roleAdapter;
        public SendSubdivCodeRequest(Handler handler, ArrayList<GetSetValues>arrayList, GetSetValues getSetValues, SubDivisionAdapter roleAdapter) {
            this.handler = handler;
            this.arrayList = arrayList;
            this.getSetValues = getSetValues;
            this.roleAdapter = roleAdapter;
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                //response = UrlPostConnection("http://bc_service.hescomtrm.com/Service.asmx/Subdivision_Details");
                response = UrlPostConnection(BASEURL + "Subdivision_Details");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            recevierData.getSubdivdetails(result,handler,arrayList,getSetValues, roleAdapter);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class ConnectURL extends AsyncTask<String,String,String>{
        String response;
        Handler handler;
        ArrayList<TimeStampValues> timeStampValuesArrayList;
        public ConnectURL( Handler handler,ArrayList<TimeStampValues> timeStampValuesArrayList){
            this.handler = handler;
            this.timeStampValuesArrayList = timeStampValuesArrayList;
        }
        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> datamap = new HashMap<>();

            datamap.put("subdivcode", strings[0]);
            datamap.put("Ddate", strings[1]);

            try {
                response = UrlPostConnection(BASE_URL_BILLEDUNBILLED + "BilledUnbilledDetails",datamap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            recevierData.getBilledUnbilledDetails(s,handler,timeStampValuesArrayList);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class BillingFileSummary extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;
        Billing_Summary getSetValues;
        ArrayList<Billing_Summary> arrayList;

        public BillingFileSummary(Handler handler, Billing_Summary getSetValues, ArrayList<Billing_Summary> arrayList) {
            this.handler = handler;
            this.getSetValues = getSetValues;
            this.arrayList = arrayList;
        }



        @Override
        protected String doInBackground(String... params) {
            HashMap<String,String> datamap = new HashMap<>();
            datamap.put("subdivcode", params[0]);
            datamap.put("fromdate",params[1]);
            datamap.put("todate",params[2]);
            try {
                //response = urlPostConnection("http://bc_service.hescomtrm.com/Service.asmx/FilesCount",datamap);
                response = UrlPostConnection(BASEURL + "FilesCount",datamap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            recevierData.getBillingSummary(result,handler,getSetValues,arrayList);
        }
    }

    public class MRTracking extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues>arrayList;
        MRLocation_Adapter mrAdapter;

        public MRTracking(Handler handler, ArrayList<GetSetValues>arrayList,GetSetValues getSetValues,MRLocation_Adapter mrAdapter)
        {
            this.handler = handler;
            this.getSetValues = getSetValues;
            this.arrayList = arrayList;
            this.mrAdapter = mrAdapter;
        }
        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("SubDivCode", params[0]);
            try {
                response = UrlPostConnection(BASEURL + "LGLTMRDETAILS", datamap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            recevierData.getMrTracking_Summary(result, handler,arrayList,getSetValues,mrAdapter);
        }
    }

}
