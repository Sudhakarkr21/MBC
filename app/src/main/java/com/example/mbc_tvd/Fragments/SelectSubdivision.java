package com.example.mbc_tvd.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

import com.example.mbc_tvd.Adapters.RoleAdapter;
import com.example.mbc_tvd.Adapters.SubDivisionAdapter;
import com.example.mbc_tvd.MainActivity;
import com.example.mbc_tvd.Main_Activity2;
import com.example.mbc_tvd.Others.FunctionCall;
import com.example.mbc_tvd.Posting.SendingData;
import com.example.mbc_tvd.R;
import com.example.mbc_tvd.Values.GetSetValues;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.example.mbc_tvd.Others.Constant.SHOWMR;
import static com.example.mbc_tvd.Others.Constant.SUBDIV_DETAILS_FAILURE;
import static com.example.mbc_tvd.Others.Constant.SUBDIV_DETAILS_SUCCESS;


public class SelectSubdivision extends Fragment {

    Toolbar toolbar;
    Spinner spinner;
    EditText edt_date;
    Button btn_report;
    View view;
    int year, month, date;
    FragmentTransaction fragmentTransaction;
    String main_selected_date, main_role, day, dd;
    ArrayList<GetSetValues> getSetValuesList;
    SubDivisionAdapter subDivisionAdapter;
    SendingData sendingData;
    ProgressDialog progressDialog;
    GetSetValues getSetValues;
    FunctionCall functionCall = new FunctionCall();

    public SelectSubdivision() {
        // Required empty public constructor
    }

    @SuppressLint("HandlerLeak")
    private Handler
            handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUBDIV_DETAILS_SUCCESS:
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    break;
                case SUBDIV_DETAILS_FAILURE:
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Failure!!", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_select_subdivision, container, false);
        initializer();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });

        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(main_role)){
                    if(!TextUtils.isEmpty(main_selected_date)){
                        ((Main_Activity2) Objects.requireNonNull(getActivity())).switchBilledContent(Main_Activity2.Steps.FORM1, main_role, main_selected_date, day,dd);
                    }else Toast.makeText(getActivity(),"Enter Date",Toast.LENGTH_LONG).show();
                }else Toast.makeText(getActivity(),"Select Subdivision",Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    public void initializer() {

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("MR Time Stamp");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        btn_report = view.findViewById(R.id.reports);
        edt_date = view.findViewById(R.id.date);
        spinner = view.findViewById(R.id.spinner1);
        getSetValues = new GetSetValues();
        progressDialog = new ProgressDialog(getActivity());
        getSetValuesList = new ArrayList<>();
        subDivisionAdapter = new SubDivisionAdapter(getSetValuesList, getActivity());
        spinner.setAdapter(subDivisionAdapter);
        sendingData = new SendingData(Objects.requireNonNull(getActivity()));
        functionCall.progressDailog(progressDialog, "Fetching Subdivisions", "Please Wait..", getActivity());
        SendingData.SendSubdivCodeRequest sendSubdivCodeRequest = sendingData.new SendSubdivCodeRequest(handler, getSetValuesList, getSetValues, subDivisionAdapter);
        sendSubdivCodeRequest.execute();
        spinner.setSelection(0);

        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                date = cal.get(Calendar.DAY_OF_MONTH);
                day = date + "";
                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        dateSetListener, year, month, date);
                dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                dialog.show();
            }
        });

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
    }

    public DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Date Starttime = null;
            edt_date.setText("");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            try {
                Starttime = new SimpleDateFormat("dd-MM-yyyy", Locale.US).parse(("" + dayOfMonth + "-" + "" + (monthOfYear + 1) + "-" + "" + year));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String dateselected = sdf.format(Starttime);
            Log.d("date", dateselected);
            edt_date.setText(dateselected);
            edt_date.setSelection(edt_date.getText().length());
            main_selected_date = edt_date.getText().toString();
        }
    };
}
