package com.example.mbc_tvd;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;

import com.example.mbc_tvd.Fragments.Billing_File_Summary;
import com.example.mbc_tvd.Fragments.MRTracking;
import com.example.mbc_tvd.Fragments.MRTracking_fragment;
import com.example.mbc_tvd.Fragments.MR_Details_Display;
import com.example.mbc_tvd.Fragments.SelectSubdivision;
import com.example.mbc_tvd.Fragments.ShowMRDetails;
import com.example.mbc_tvd.Fragments.SummaryDetails;
import com.example.mbc_tvd.Values.GetSetValues;
import com.example.mbc_tvd.Values.TimeStampValues;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.mbc_tvd.Others.Constant.BILLINGFILESUMMARY;
import static com.example.mbc_tvd.Others.Constant.LAYOUT;
import static com.example.mbc_tvd.Others.Constant.MRTRACKING;
import static com.example.mbc_tvd.Others.Constant.SENDSUBDIV;
import static com.example.mbc_tvd.Others.Constant.SHOWMR;

public class Main_Activity2 extends AppCompatActivity {
    String layout;
    ConstraintLayout main_layout;
    GetSetValues getSetValues;
    List<GetSetValues> getSetValuesList;

    private Fragment fragment;
    private FragmentManager fragmentManager;

    public enum Steps {
        FORM0(SelectSubdivision.class),
        FORM1(ShowMRDetails.class),
        FORM2(Billing_File_Summary.class),
        FORM3(SummaryDetails.class),
        FORM4(MRTracking_fragment.class),
        FORM5(MRTracking.class),
        FORM6(MR_Details_Display.class);
        private Class clazz;

        Steps(Class clazz) {
            this.clazz = clazz;
        }

        public Class getFragClass() {
            return clazz;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initialize();

        Intent intent = getIntent();
        layout = Objects.requireNonNull(intent.getExtras()).getString(LAYOUT);

        if (!TextUtils.isEmpty(layout)) {
            switch (layout) {
                case SENDSUBDIV:
                    switchPopBackContent(Steps.FORM0);
                    break;
                case SHOWMR:
                    switchPopBackContent(Steps.FORM1);
                    break;
                case BILLINGFILESUMMARY:
                    switchPopBackContent(Steps.FORM2);
                    break;
                case MRTRACKING:
                    switchPopBackContent(Steps.FORM4);
                    break;
            }
        }
    }

    public void initialize() {
        getSetValues = new GetSetValues();
        main_layout = findViewById(R.id.main_container);
        fragmentManager = getSupportFragmentManager();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void switchPopBackContent(Steps currentForm) {
        try {
            fragment = (Fragment) currentForm.getFragClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.main_container, fragment, currentForm.name());
        ft.commit();
    }

    public void switchBilledContent(Steps currentForm, String subdivcode, String date, String day, String dd) {
        Bundle bundle = new Bundle();

        try {
            fragment = (Fragment) currentForm.getFragClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        bundle.putString("subdivcode", subdivcode);
        bundle.putString("date", date);
        bundle.putString("dd", dd);
        bundle.putString("daycount", day);
        fragment.setArguments(bundle);
        FragmentTransaction ft = fragmentManager.beginTransaction();
//        ft.setCustomAnimations(R.anim.slide_down,R.anim.slide_up);
        ft.replace(R.id.main_container, fragment, currentForm.name());
        ft.addToBackStack(currentForm.name());
        ft.commit();
    }

    public void switchfilesummary(Steps currentForm, String subdivcode, String fromdate, String todate) {
        Bundle bundle = new Bundle();

        try {
            fragment = (Fragment) currentForm.getFragClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        bundle.putString("subdivcode", subdivcode);
        bundle.putString("fromdate", fromdate);
        bundle.putString("todate", todate);

        fragment.setArguments(bundle);
        FragmentTransaction ft = fragmentManager.beginTransaction();
//        ft.setCustomAnimations(R.anim.slide_down,R.anim.slide_up);
        ft.replace(R.id.main_container, fragment, currentForm.name());
        ft.addToBackStack(currentForm.name());
        ft.commit();
    }

    public void mr_details_display(Steps currentForm, ArrayList<TimeStampValues> timeStampValues, int pos) {
        Bundle bundle = new Bundle();

        try {
            fragment = (Fragment) currentForm.getFragClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        bundle.putInt("pos", pos);
        bundle.putSerializable("arraylist", (Serializable)timeStampValues);

        fragment.setArguments(bundle);
        FragmentTransaction ft = fragmentManager.beginTransaction();
//        ft.setCustomAnimations(R.anim.slide_down,R.anim.slide_up);
        ft.replace(R.id.main_container, fragment, currentForm.name());
        ft.addToBackStack(currentForm.name());
        ft.commit();
    }


}
