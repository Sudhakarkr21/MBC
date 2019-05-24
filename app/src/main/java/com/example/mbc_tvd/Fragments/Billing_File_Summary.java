package com.example.mbc_tvd.Fragments;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbc_tvd.Adapters.SubDivisionAdapter;
import com.example.mbc_tvd.Main_Activity2;
import com.example.mbc_tvd.Others.FunctionCall;
import com.example.mbc_tvd.Posting.SendingData;
import com.example.mbc_tvd.R;
import com.example.mbc_tvd.Values.GetSetValues;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.example.mbc_tvd.Others.Constant.SUBDIV_DETAILS_FAILURE;
import static com.example.mbc_tvd.Others.Constant.SUBDIV_DETAILS_SUCCESS;

public class Billing_File_Summary extends Fragment {

    Button button;
    Spinner spinner;
    Toolbar toolbar;
    EditText edt_from_date,edt_to_date;
    View view;
    ArrayList<GetSetValues> getSetValuesList;
    SubDivisionAdapter subDivisionAdapter;
    ProgressDialog progressDialog;
    SendingData sendingData;
    FunctionCall functionCall;
    GetSetValues getSetValues;
    String main_role,from_date,day,to_date,day1;
    int year, month, date;


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case SUBDIV_DETAILS_SUCCESS:
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    break;
                case SUBDIV_DETAILS_FAILURE:
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Failure!!", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    public Billing_File_Summary() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_billing__file__summary, container, false);
        initailize();

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });
        return view;
    }

    public void initailize(){
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Billing FIle Summary");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        button = view.findViewById(R.id.billing_file_summary_reports);
        spinner = view.findViewById(R.id.spinner2);
        functionCall = new FunctionCall();
        getSetValuesList = new ArrayList<>();
        progressDialog = new ProgressDialog(getActivity());
        getSetValuesList = new ArrayList<>();
        subDivisionAdapter = new SubDivisionAdapter(getSetValuesList, getActivity());
        spinner.setAdapter(subDivisionAdapter);
        edt_from_date = view.findViewById(R.id.fromdate);
        edt_to_date = view.findViewById(R.id.todate);

        sendingData = new SendingData(Objects.requireNonNull(getActivity()));
        functionCall.progressDailog(progressDialog, "Fetching Subdivisions", "Please Wait..", getActivity());
        SendingData.SendSubdivCodeRequest sendSubdivCodeRequest = sendingData.new SendSubdivCodeRequest(handler, getSetValuesList, getSetValues, subDivisionAdapter);
        sendSubdivCodeRequest.execute();
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tvrole2 = view.findViewById(R.id.spinner_txt);

                String role = tvrole2.getText().toString();
                main_role = role.substring(0, 6);
                Toast.makeText(getActivity(), main_role, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        edt_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                date = cal.get(Calendar.DAY_OF_MONTH);
                day = date + "";
                DatePickerDialog dialog = new DatePickerDialog(Objects.requireNonNull(getActivity()),
                        dateSetListener, year, month, date);


                dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                dialog.show();
            }
        });

        edt_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                date = cal.get(Calendar.DAY_OF_MONTH);
                day1 = date + "";
                DatePickerDialog dialog = new DatePickerDialog(Objects.requireNonNull(getActivity()),
                        dateSetListener1, year, month, date);

                dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                dialog.show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!StringUtils.isEmpty(main_role)){
                    if(!StringUtils.isEmpty(from_date)){
                        if(!StringUtils.isEmpty(to_date)){
                            ((Main_Activity2) Objects.requireNonNull(getActivity())).switchfilesummary(Main_Activity2.Steps.FORM3,main_role,from_date,to_date);
                        }else Toast.makeText(getActivity(),"Enter To Date",Toast.LENGTH_LONG).show();
                    }else Toast.makeText(getActivity(),"Enter From Date",Toast.LENGTH_LONG).show();
                }else Toast.makeText(getActivity(),"Select MR Code",Toast.LENGTH_LONG).show();
            }
        });

    }

    public DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Date Starttime = null;
            edt_from_date.setText("");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            try {
                Starttime = new SimpleDateFormat("dd-MM-yyyy", Locale.US).parse(("" + dayOfMonth + "-" + "" + (monthOfYear + 1) + "-" + "" + year));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String dateselected = sdf.format(Starttime);
            Log.d("date", dateselected);
            edt_from_date.setText(dateselected);
            edt_from_date.setSelection(edt_from_date.getText().length());
            from_date = edt_from_date.getText().toString();
        }
    };

    public DatePickerDialog.OnDateSetListener dateSetListener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Date Starttime = null;
            edt_to_date.setText("");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            try {
                Starttime = new SimpleDateFormat("dd-MM-yyyy", Locale.US).parse(("" + dayOfMonth + "-" + "" + (monthOfYear + 1) + "-" + "" + year));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String dateselected = sdf.format(Starttime);
            Log.d("date", dateselected);
            edt_to_date.setText(dateselected);
            edt_to_date.setSelection(edt_to_date.getText().length());
            to_date = edt_to_date.getText().toString();
        }
    };
}
