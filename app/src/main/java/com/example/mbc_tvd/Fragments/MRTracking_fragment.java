package com.example.mbc_tvd.Fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbc_tvd.Adapters.SubDivisionAdapter;
import com.example.mbc_tvd.Main_Activity2;
import com.example.mbc_tvd.Others.FunctionCall;
import com.example.mbc_tvd.Posting.SendingData;
import com.example.mbc_tvd.R;
import com.example.mbc_tvd.Values.GetSetValues;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.mbc_tvd.Others.Constant.SUBDIV_DETAILS_FAILURE;
import static com.example.mbc_tvd.Others.Constant.SUBDIV_DETAILS_SUCCESS;

public class MRTracking_fragment extends Fragment {

    View view;
    Toolbar toolbar;
    Spinner spinner;
    Button btn_report;
    String main_role;
    SubDivisionAdapter subDivisionAdapter;
    ArrayList<GetSetValues> getSetValuesList;
    SendingData sendingData;
    ProgressDialog progressDialog;
    GetSetValues getSetValues;
    FunctionCall functionCall = new FunctionCall();

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      view = inflater.inflate(R.layout.fragment_mrtracking_fragment, container, false);
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
                ((Main_Activity2) Objects.requireNonNull(getActivity())).switchfilesummary(Main_Activity2.Steps.FORM5,main_role,"0","0");
            }
        });

        return view;
    }

    public void initializer() {
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("MR Tracking");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        btn_report = view.findViewById(R.id.reports);
        spinner = view.findViewById(R.id.spinner3);
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
}
