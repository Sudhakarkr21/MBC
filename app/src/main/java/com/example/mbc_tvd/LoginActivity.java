package com.example.mbc_tvd;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.button.MaterialButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbc_tvd.Adapters.RoleAdapter;
import com.example.mbc_tvd.Others.FunctionCall;
import com.example.mbc_tvd.Posting.SendingData;
import com.example.mbc_tvd.Values.GetSetValues;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.mbc_tvd.Others.Constant.DLG_LOGIN;
import static com.example.mbc_tvd.Others.Constant.LOGIN_FAILURE;
import static com.example.mbc_tvd.Others.Constant.LOGIN_SUCCESS;
import static com.example.mbc_tvd.Others.Constant.PREF_NAME;
import static com.example.mbc_tvd.Others.Constant.sPref_ROLE;
import static com.example.mbc_tvd.Others.Constant.sPref_SUBDIVISION;

public class LoginActivity extends AppCompatActivity {
    MaterialButton bt_login;
    CheckBox checkBox;
    GetSetValues getSetValues;
    RoleAdapter roleAdapter;
    Spinner role_spinner;
    String login_role,code,device_id,password,current_version="0";
    ArrayList<GetSetValues> roles_list;
    FunctionCall functionCall;
    ProgressDialog progressDialog;
    SendingData sendingData;
    AlertDialog alertDialog;
    SharedPreferences.Editor editor;
    SharedPreferences sPref;
    TextView version_code;

    Handler handler = new Handler(new Handler.Callback() {
        @SuppressLint("SetTextI18n")
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case LOGIN_SUCCESS:
                    SavePreferences("Username", code);
                    SavePreferences("Password", password);
                    progressDialog.dismiss();
                    alertDialog.dismiss();
                    editor.putString(sPref_ROLE, login_role);
                    editor.putString(sPref_SUBDIVISION, getSetValues.getSUBDIVCODE());
                    editor.commit();
                    move_to_next_activity();
                    break;
                case LOGIN_FAILURE:
                    progressDialog.dismiss();
                    alertDialog.dismiss();
                    View inflater = getLayoutInflater().inflate(R.layout.toast_layout,(ViewGroup) findViewById(R.id.toast_layout));

                    Toast toast = new Toast(getApplicationContext());
                    ImageView imageView = inflater.findViewById(R.id.image);
                    imageView.setImageResource(R.drawable.wrong);
                    TextView textView = inflater.findViewById(R.id.text1);
                    textView.setText("Invalid credentials");
                    toast.setGravity(Gravity.BOTTOM,0,0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(inflater);
                    toast.show();

                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initailize();

        for (int i = 0; i < getResources().getStringArray(R.array.login_role3).length; i++) {
            getSetValues = new GetSetValues();
            getSetValues.setLogin_role(getResources().getStringArray(R.array.login_role3)[i]);
            roles_list.add(getSetValues);
            roleAdapter.notifyDataSetChanged();
        }

        role_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                GetSetValues roledetails = roles_list.get(position);
                login_role = roledetails.getLogin_role();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });



        PackageInfo packageInfo;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            current_version = packageInfo.versionName;
            version_code.setText(current_version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (tm != null)
                    device_id = tm.getDeviceId();
                if(functionCall.isInternetOn(LoginActivity.this)){
                    if (!TextUtils.equals(login_role,"--SELECT--")){
                        showdialog(DLG_LOGIN);
//                        move_to_next_activity();
                    }else Toast.makeText(getApplicationContext(),"Please Select Login Role!!",Toast.LENGTH_LONG).show();

                }else Toast.makeText(getApplicationContext(),"Please connect to internet..",Toast.LENGTH_LONG).show();
            }
        });
    }
    private void move_to_next_activity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("subdivcode", code);
        intent.putExtra("versioncode",current_version);
        startActivity(intent);
        finish();
    }

    public void initailize(){
        version_code = findViewById(R.id.txt_version_code);
        progressDialog = new ProgressDialog(this);
        bt_login = findViewById(R.id.bt_login);
        checkBox = findViewById(R.id.checkbox);
        role_spinner = findViewById(R.id.spinner);
        roles_list = new ArrayList<>();
        roleAdapter = new RoleAdapter(roles_list, this);
        role_spinner.setAdapter(roleAdapter);
        functionCall = new FunctionCall();
        sPref = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        editor = sPref.edit();
        editor.apply();
        SavePreferences("TEST_REAL_SERVER","REAL");
        sendingData = new SendingData(LoginActivity.this);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                SavePreferences("TEST_REAL_SERVER","TEST");
                sendingData = new SendingData(LoginActivity.this);
            }else {
                SavePreferences("TEST_REAL_SERVER","REAL");
                sendingData = new SendingData(LoginActivity.this);
            }
        });
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void showdialog(int id){
        switch (id){
            case DLG_LOGIN:
                AlertDialog.Builder login_dlg = new AlertDialog.Builder(this);
                login_dlg.setCancelable(false);
                    LinearLayout linearLayout = (LinearLayout) this.getLayoutInflater().inflate(R.layout.dailog_login_layout,null);
                login_dlg.setView(linearLayout);
                final EditText user_dlg = linearLayout.findViewById(R.id.et_login_id);
                final EditText passward_dlg = linearLayout.findViewById(R.id.et_login_passward);
                Button cancel_dlg_button = linearLayout.findViewById(R.id.dialog_negative_btn);
                Button login_dlg_button = linearLayout.findViewById(R.id.dialog_positive_btn);
                user_dlg.requestFocus();

                alertDialog = login_dlg.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        login_dlg_button.setOnClickListener(v -> {
                            code = user_dlg.getText().toString();
                            if(functionCall.isInternetOn(LoginActivity.this)){
                                if(!TextUtils.isEmpty(code)){
                                    if(!TextUtils.isEmpty(passward_dlg.getText().toString())){
                                        password = passward_dlg.getText().toString();
                                        functionCall.progressDailog(progressDialog,"Checking Credentials","Please Wait..",LoginActivity.this);
                                        if (!StringUtils.startsWithIgnoreCase(login_role, "AAO")) {
                                            SendingData.Login login = sendingData.new Login(getSetValues, handler);
                                            login.execute(code, password);
                                        } else {
                                            SendingData.MR_Login login = sendingData.new MR_Login(handler, getSetValues);
                                            login.execute(code, device_id, password);
                                        }
                                    }else Toast.makeText(LoginActivity.this,"Enter Passward!!",Toast.LENGTH_LONG).show();
                                }else user_dlg.setError("Enter Subdiv code!!");
                            }else Toast.makeText(LoginActivity.this, "Please Connect to Internet!!", Toast.LENGTH_SHORT).show();

                        });

                        cancel_dlg_button.setOnClickListener(v -> {
                            alertDialog.dismiss();
                        });
                    }
                });
                (Objects.requireNonNull(alertDialog.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
                alertDialog.show();

                break;
        }
    }

}
