package com.example.mbc_tvd.Fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbc_tvd.Adapters.MRDetailsAdapter;
import com.example.mbc_tvd.Others.FunctionCall;
import com.example.mbc_tvd.Posting.SendingData;
import com.example.mbc_tvd.R;
import com.example.mbc_tvd.Values.TimeStampValues;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static com.example.mbc_tvd.Others.Constant.BILLED_UNBILLED_FAILURE;
import static com.example.mbc_tvd.Others.Constant.BILLED_UNBILLED_SUCCESS;


public class ShowMRDetails extends Fragment {
    Toolbar toolbar;
    TextView tv_subdiv_code,tv_date,tv_total_mr_count,tv_records,tv_downloads,tv_billed,tv_unbilled;
    View view;
    String subdivisioncode="0",dum="0",day="0",dd = "0",date1,substringstorecurrentnextday;
    ArrayList<TimeStampValues> timeStampValuesArrayList;
    TimeStampValues timeStampValues;
    ArrayList<TimeStampValues> result ;
    SendingData sendingData;
    FunctionCall functionCall;
    RecyclerView recyclerView;
    MRDetailsAdapter mrdetailsAdapter;
    ProgressDialog progressDialog;
    int total_download,pd_billed_records,d_billed_records,bip_val,total_billed,total_unbilled;
    BottomNavigationView bottomNavigationView;
    ShimmerFrameLayout container1;
    LinearLayout linearLayout;
    TableLayout tableLayout;
    int updatenextint=0;
    public ShowMRDetails() {
        // Required empty public constructor
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what){
                case BILLED_UNBILLED_SUCCESS:
//                    progressDialog.dismiss();
                    container1.stopShimmerAnimation();
                    result.clear();
                    for(int i=0;i<timeStampValuesArrayList.size();i++){
                        TimeStampValues timeStampValues1 = new TimeStampValues();

                        if (timeStampValuesArrayList.get(i).getStatus().equals("D")) {
                            total_download = total_download + Integer.parseInt(timeStampValuesArrayList.get(i).getDownload_Record());
                            pd_billed_records = pd_billed_records + Integer.parseInt(timeStampValuesArrayList.get(i).getDownload_Record());
                        } else {
                            d_billed_records = d_billed_records + Integer.parseInt(timeStampValuesArrayList.get(i).getDownload_Record());
                        }

                        if(timeStampValuesArrayList.get(i).getBipcount().equals("")){
                            bip_val = bip_val + 0;
                        }else bip_val = bip_val + Integer.parseInt(timeStampValuesArrayList.get(i).getBipcount());


                        if(timeStampValuesArrayList.get(i).getBilled_Record().equals("")){
                            timeStampValues1.setBilled_Record("NA");
                            total_billed = total_billed + 0;
                        }else{
                            timeStampValues1.setBilled_Record(timeStampValuesArrayList.get(i).getBilled_Record());
                            total_billed = total_billed + Integer.parseInt(timeStampValuesArrayList.get(i).getBilled_Record());
                        }

//                        --------------FETCHING DOWNLOAD TIME-------------------
                            if(timeStampValuesArrayList.get(i).getDownload_DateTime().equals(""))
                                timeStampValues1.setDownload_DateTime("NA");
                            else {
                                String part2 = timeStampValuesArrayList.get(i).getDownload_DateTime().substring(timeStampValuesArrayList.get(i).getDownload_DateTime().indexOf(" ") + 1, 16);
                                timeStampValues1.setDownload_DateTime(part2);
                            }

                        /******FETCHING ONLY BILLEDTIME*************/

                        if(timeStampValuesArrayList.get(i).getBilled_DateTime().equals(""))
                            timeStampValues1.setBilled_DateTime("NA");
                        else {
                            String part2 = timeStampValuesArrayList.get(i).getBilled_DateTime().substring(timeStampValuesArrayList.get(i).getBilled_DateTime().indexOf(" ") + 1, 16);
                            timeStampValues1.setBilled_DateTime(part2);
                        }

                        /******CHECKING DOWNLOAD RECORD IS EMPTY OR NOT*****/
                        if(timeStampValuesArrayList.get(i).getDownload_Record().equals(""))
                            timeStampValues1.setDownload_Record("NA");
                        else {
                            timeStampValues1.setDownload_Record(timeStampValuesArrayList.get(i).getDownload_Record());
                        }
                        /*****FOR UNBILLED************/
                        if(timeStampValuesArrayList.get(i).getBilled_Record().equals("")){
                            timeStampValues1.setUnBilled_Record(timeStampValuesArrayList.get(i).getBilled_Record());
                            total_unbilled = total_unbilled + Integer.parseInt(timeStampValuesArrayList.get(i).getBilled_Record());
                        }else {
                            int val = Integer.parseInt(timeStampValuesArrayList.get(i).getDownload_Record()) - Integer.parseInt(timeStampValuesArrayList.get(i).getBilled_Record());
                            timeStampValues1.setUnBilled_Record(val + "");
                            total_unbilled = total_unbilled + val;
                        }

                        if (timeStampValuesArrayList.get(i).getStatus().equals("D")) {
                            timeStampValues1.setStatus("D");
                        } else {
                            timeStampValues1.setStatus("PD");
                        }

                        timeStampValues1.setMRCode(timeStampValuesArrayList.get(i).getMRCode());
                        result.add(timeStampValues1);
                        mrdetailsAdapter.notifyDataSetChanged();
                    }


                    int size = timeStampValuesArrayList.size();
                    tv_subdiv_code.setText(subdivisioncode);
                    tv_total_mr_count.setText(String.valueOf(size));
                    tv_records.setText(String.valueOf(bip_val));
                    tv_downloads.setText(String.valueOf(total_download));
                    tv_billed.setText(String.valueOf(total_billed));
                    tv_unbilled.setText(String.valueOf(total_unbilled));
                    tv_date.setText(functionCall.Parse_Date(date1));
//                    Toast.makeText(getActivity(),timeStampValuesArrayList.get(0).getBipcount(),Toast.LENGTH_LONG).show();
                    break;
                case BILLED_UNBILLED_FAILURE:
                    linearLayout.setVisibility(View.GONE);
//                    tableLayout.setVisibility(View.GONE);
                    container1.stopShimmerAnimation();
//                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),"No records",Toast.LENGTH_LONG).show();
                    break;
            }
            return false;
        }
    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_show_mrdetails, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            subdivisioncode = bundle.getString("subdivcode");
            dum = bundle.getString("date");
            day = bundle.getString("daycount");
            dd = bundle.getString("dd");
        }

        container1 = view.findViewById(R.id.shimmer_view_container1);
        container1.startShimmerAnimation();
        initialize();

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });

        date1 = functionCall.Parse_Date2(dum);
//        functionCall.progressDailog(progressDialog,"Fetching the data","Please Wait!!",getActivity());

        sendingData = new SendingData(Objects.requireNonNull(getActivity()));
        SendingData.ConnectURL connectURL = sendingData.new ConnectURL(handler,timeStampValuesArrayList);
        connectURL.execute(subdivisioncode,date1);


        return view ;
    }

    public void initialize(){
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("MR Reports");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        functionCall = new FunctionCall();
        timeStampValuesArrayList = new ArrayList<>();
        timeStampValues = new TimeStampValues();


        linearLayout = view.findViewById(R.id.layout);
//        tableLayout = view.findViewById(R.id.tablayout);
        progressDialog = new ProgressDialog(getActivity());
        result = new ArrayList<>();
        tv_subdiv_code = view.findViewById(R.id.subdiv_code);
        tv_date = view.findViewById(R.id.date);
        tv_total_mr_count = view.findViewById(R.id.total_mr_count);
        tv_records = view.findViewById(R.id.total_records);
        tv_downloads = view.findViewById(R.id.total_downloads);
        tv_billed = view.findViewById(R.id.total_billed);
        tv_unbilled = view.findViewById(R.id.total_unbilled);

        recyclerView =  view.findViewById(R.id.mrdetails_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        mrdetailsAdapter = new MRDetailsAdapter(getActivity(), result);
        //setting the adapter
        recyclerView.setAdapter(mrdetailsAdapter);


        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    int i = menuItem.getItemId();

                    switch (i){
                        case R.id.action_previous:
                            onPrevious();
                            break;
                        case R.id.action_next:
                            onNext();
                            break;
                        case R.id.action_print:
                            break;
                    }
                    return true;
                }
            };

        public void onNext(){

            try {


                if(updatenextint >= 15) {
                    Toast.makeText(getActivity(), "After 15 no billing", Toast.LENGTH_LONG).show();
                    return;
                }else {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar c = Calendar.getInstance();
                    c.setTime(sdf.parse(date1));
                    c.add(Calendar.DATE, 1);  // number of days to add
                    date1 = sdf.format(c.getTime());
                    substringstorecurrentnextday = date1.substring(8, 10);
                    updatenextint = Integer.parseInt(substringstorecurrentnextday);
                    int max_val = Integer.parseInt(day);
                    SendingData.ConnectURL connectURL = sendingData.new ConnectURL(handler,timeStampValuesArrayList);
                    connectURL.execute(subdivisioncode,date1);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public void onPrevious(){
            try {


                if(updatenextint >= 15) {
                    Toast.makeText(getActivity(), "After 15 no billing", Toast.LENGTH_LONG).show();
                    return;
                }else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar c = Calendar.getInstance();
                    c.setTime(sdf.parse(date1));
                    c.add(Calendar.DATE, -1);  // number of days to add
                    date1 = sdf.format(c.getTime());
                    substringstorecurrentnextday = date1.substring(8, 10);
                    updatenextint = Integer.parseInt(substringstorecurrentnextday);
                    int max_val = Integer.parseInt(day);
                    SendingData.ConnectURL connectURL = sendingData.new ConnectURL(handler,timeStampValuesArrayList);
                    connectURL.execute(subdivisioncode,date1);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
}
