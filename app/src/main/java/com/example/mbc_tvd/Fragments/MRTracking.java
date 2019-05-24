package com.example.mbc_tvd.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mbc_tvd.Adapters.MRLocation_Adapter;
import com.example.mbc_tvd.Others.FunctionCall;
import com.example.mbc_tvd.Posting.SendingData;
import com.example.mbc_tvd.R;
import com.example.mbc_tvd.Values.GetSetValues;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.mbc_tvd.Others.Constant.MRTRACKING_FAILURE;
import static com.example.mbc_tvd.Others.Constant.MRTRACKING_SUCCESS;

public class MRTracking extends Fragment {

    View view;
    Toolbar toolbar;
    String subdivisioncode;
    ArrayList<GetSetValues> arrayList;
    GetSetValues getSetValues, getSet;
    RecyclerView recyclerView;
    MRLocation_Adapter mrLocation_adapter;
    FunctionCall functionCall;
    SendingData sendingData;
    ProgressDialog progressDialog;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case MRTRACKING_SUCCESS:
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),"Success",Toast.LENGTH_LONG).show();
                    break;
                case MRTRACKING_FAILURE:
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),"No data Found!!",Toast.LENGTH_LONG).show();
                    break;
            }
            return false;
        }
    });

   public MRTracking(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mrtracking, container, false);

        Bundle bundle = getArguments();
        if (bundle!= null)
            subdivisioncode = bundle.getString("subdivcode");

        initialize();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });

        return view;
    }

    public void initialize(){
       toolbar = view.findViewById(R.id.toolbar);
       toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
       toolbar.setTitle("MR Tracking");
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        arrayList = new ArrayList<>();
        setHasOptionsMenu(true);
        recyclerView =  view.findViewById(R.id.mrtrack_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mrLocation_adapter = new MRLocation_Adapter(getActivity(),arrayList,getSetValues);
        recyclerView.setAdapter(mrLocation_adapter);
        functionCall = new FunctionCall();
        sendingData = new SendingData(Objects.requireNonNull(getContext()));
        progressDialog = new ProgressDialog(getActivity());
        functionCall.progressDailog(progressDialog,"Fetching Data","Please wait",getActivity());
        SendingData.MRTracking mrTracking = sendingData.new MRTracking(handler,arrayList,getSetValues,mrLocation_adapter);
        mrTracking.execute(subdivisioncode);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuInflater menuInflater = Objects.requireNonNull(getActivity()).getMenuInflater();
        menuInflater.inflate(R.menu.location,menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint(Html.fromHtml("<font color = #212121>" + "Search by Mrcode.." + "</font>"));
        search(searchView);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void search(SearchView searchView)
    {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mrLocation_adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
