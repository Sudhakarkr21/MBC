package com.example.mbc_tvd.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mbc_tvd.MainActivity;
import com.example.mbc_tvd.R;

import java.util.Objects;

import static com.example.mbc_tvd.Others.Constant.BILLINGFILESUMMARY;
import static com.example.mbc_tvd.Others.Constant.MRTRACKING;
import static com.example.mbc_tvd.Others.Constant.SENDSUBDIV;


public class SendSubdivCode extends Fragment implements View.OnClickListener {

    Button btnmr_time_stamp,billing_file_summary,btn_mr_tracking;
    View view;


    public SendSubdivCode() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_send_subdiv_code, container, false);
        initialize();
        return view;
    }

    public void initialize(){
        btnmr_time_stamp = view.findViewById(R.id.mr_time_stamp);
        btnmr_time_stamp.setOnClickListener(this);

        billing_file_summary = view.findViewById(R.id.billing_file_summary);
        billing_file_summary.setOnClickListener(this);

        btn_mr_tracking = view.findViewById(R.id.mr_tracking);
        btn_mr_tracking.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i){
            case R.id.mr_time_stamp:
                ((MainActivity) Objects.requireNonNull(getActivity())).startIntent(getActivity(),SENDSUBDIV);
                break;
            case R.id.billing_file_summary:
                ((MainActivity) Objects.requireNonNull(getActivity())).startIntent(getActivity(),BILLINGFILESUMMARY);
                break;
            case R.id.mr_tracking:
                ((MainActivity) Objects.requireNonNull(getActivity())).startIntent(getActivity(),MRTRACKING);
                break;
        }
    }


}
