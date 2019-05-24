package com.example.mbc_tvd.Fragments;


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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mbc_tvd.Others.FunctionCall;
import com.example.mbc_tvd.Posting.SendingData;
import com.example.mbc_tvd.R;
import com.example.mbc_tvd.Values.Billing_Summary;
import com.example.mbc_tvd.Values.GetSetValues;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.mbc_tvd.Others.Constant.BILLING_FILE_SUMMARY_FAILURE;
import static com.example.mbc_tvd.Others.Constant.BILLING_FILE_SUMMARY_SUCCESS;


public class SummaryDetails extends Fragment {
    View view;
    String subdiv, from_date, to_date, upload1, download, valid;
    Toolbar toolbar;
    SendingData sendingData;
    GetSetValues getSetValues;
    TextView subdivision, totalvalidfile, totaldownload, notdownload, upload, notupload;
    TextView from, to;
    LinearLayout linearLayout;
    FunctionCall functionCall;
    ProgressDialog progressDialog;
    Billing_Summary billing_summary;
    ArrayList<Billing_Summary> billing_summaryArrayList;



    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case BILLING_FILE_SUMMARY_SUCCESS:
                    progressDialog.dismiss();
                    linearLayout.setVisibility(View.VISIBLE);
                    download = billing_summaryArrayList.get(0).getDWNRECORD();
                    upload1 = billing_summaryArrayList.get(0).getUPLOADRECORD();
                    valid = billing_summaryArrayList.get(0).getINFOSYSRECORD();

                    int totalvalid = Integer.parseInt(valid);
                    int totaldown = Integer.parseInt(download);
                    int notdown = (totalvalid - totaldown);
                    int uplo = Integer.parseInt(upload1);
                    int notuplo = (totaldown - uplo);

                    subdivision.setText(subdiv);
                    totalvalidfile.setText(valid);
                    totaldownload.setText(download);
                    notdownload.setText(String.valueOf(notdown));
                    upload.setText(upload1);
                    notupload.setText(String.valueOf(notuplo));
                    break;
                case BILLING_FILE_SUMMARY_FAILURE:
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "No Values Found", Toast.LENGTH_LONG).show();
                    break;
            }
            return false;
        }
    });

    public SummaryDetails() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_summary_details, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            subdiv = bundle.getString("subdivcode");
            from_date = bundle.getString("fromdate");
            to_date = bundle.getString("todate");
        }

        initailize();
        functionCall.progressDailog(progressDialog,"Fetching the Data","Please Wiat!!",getActivity());
        sendingData = new SendingData(Objects.requireNonNull(getActivity()));
        SendingData.BillingFileSummary billingFileSummary = sendingData.new BillingFileSummary(handler, billing_summary,billing_summaryArrayList);
        billingFileSummary.execute(subdiv, from_date, to_date);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });
        return view;
    }

    public void initailize() {
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Summary");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        linearLayout = view.findViewById(R.id.linearlayout);
        subdivision = view.findViewById(R.id.txtsubdivision);
        totalvalidfile = view.findViewById(R.id.txt_totalvalidfile);
        totaldownload = view.findViewById(R.id.txt_totaldownloaded);
        notdownload = view.findViewById(R.id.txt_notdownloaded);
        upload = view.findViewById(R.id.txt_uploaded);
        notupload = view.findViewById(R.id.txt_notuploaded);
        from = view.findViewById(R.id.txt_from);
        to = view.findViewById(R.id.txt_to);
        progressDialog = new ProgressDialog(getActivity());
        functionCall = new FunctionCall();
        getSetValues = new GetSetValues();
        billing_summary = new Billing_Summary();
        billing_summaryArrayList = new ArrayList<>();

        from.setText(from_date);
        to.setText(to_date);
    }
}
