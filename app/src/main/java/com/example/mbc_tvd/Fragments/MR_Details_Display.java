package com.example.mbc_tvd.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mbc_tvd.R;
import com.example.mbc_tvd.Values.TimeStampValues;

import java.util.ArrayList;
import java.util.Objects;

public class MR_Details_Display extends Fragment {
    View view;
    Toolbar toolbar;
    TextView tv_mrcode, tv_downloadedrecord, tv_downloadedtime, tv_billed,
            tv_billed_time, tv_status, tv_unbilled, tv_phonenumber;

    String mrcode, downloaded_record, downloaded_time, billed, billed_time, status, unbilled, phonenumber;
    ArrayList<TimeStampValues> arrayList;
    int pos =0;
    public MR_Details_Display() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mrdetails_display, container, false);
        Bundle bundle = getArguments();
        arrayList = new ArrayList<>();
        if (bundle != null) {
            arrayList.clear();
            pos = bundle.getInt("pos");
            arrayList = (ArrayList<TimeStampValues>) bundle.getSerializable("arraylist");

            mrcode = arrayList.get(pos).getMRCode();
            downloaded_record = arrayList.get(pos).getDownload_Record();
            downloaded_time = arrayList.get(pos).getDownload_DateTime();
            billed = arrayList.get(pos).getBilled_Record();
            billed_time = arrayList.get(pos).getBilled_DateTime();
            status = arrayList.get(pos).getStatus();
            unbilled = arrayList.get(pos).getUnBilled_Record();
            phonenumber = "9050152895";
        }
        initailize();

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
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle(mrcode);

        tv_mrcode = view.findViewById(R.id.txt_mrcode);
        tv_downloadedrecord = view.findViewById(R.id.txt_downloaded_record);
        tv_downloadedtime = view.findViewById(R.id.txt_download_time);
        tv_billed = view.findViewById(R.id.txt_billed_record);
        tv_billed_time = view.findViewById(R.id.txt_billed_time);
        tv_status = view.findViewById(R.id.txt_status);
        tv_unbilled = view.findViewById(R.id.txt_unbilled_record);
        tv_phonenumber = view.findViewById(R.id.text_phone_number);

        tv_mrcode.setText(mrcode);
        tv_downloadedrecord.setText(downloaded_record);
        tv_downloadedtime.setText(downloaded_time);
        tv_billed.setText(billed_time);
        tv_billed_time.setText(billed_time);
        tv_status.setText(status);
        tv_unbilled.setText(unbilled);
        tv_phonenumber.setText(phonenumber);
    }
}
