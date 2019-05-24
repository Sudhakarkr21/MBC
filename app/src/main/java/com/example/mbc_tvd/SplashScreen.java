package com.example.mbc_tvd;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.mbc_tvd.Others.CheckInternetConnection;
import com.example.mbc_tvd.Others.FunctionCall;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashScreen extends AppCompatActivity {
    public static final int RequestPermissionCode = 1;
    CheckInternetConnection checkInternetConnection = new CheckInternetConnection(this);
    FunctionCall functionCall = new FunctionCall();
    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
//        checkInternetConnection = new CheckInternetConnection(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        if(checkInternetConnection.isConnectingToInternet())
            splash();
        else showAlertDailog(SplashScreen.this,"Internet Connection",
                "You don't have internet connection, Please try again",false);

    }

  public void splash(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkPermissions();
            }
        }, 2000);
    }


    public void startup() {
        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void checkPermissions() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= 23) {
            if (checkPermission()) {
                startup();
            } else {
                requestPermission();
            }
        } else {
            startup();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(SplashScreen.this, new String[]
                {
                        READ_PHONE_STATE,
                        WRITE_EXTERNAL_STORAGE,
                        ACCESS_FINE_LOCATION,
                        CAMERA
                }, RequestPermissionCode);
    }

    private boolean checkPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FifthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean ReadPhoneStatePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadStoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadLocationPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadCameraPermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    if (ReadPhoneStatePermission && ReadStoragePermission && ReadLocationPermission && ReadCameraPermission) {
                        startup();
                    } else {
                        Toast.makeText(SplashScreen.this, "Required All Permissions..", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                break;
        }
    }

    public void showAlertDailog(Context context,String title,String message,Boolean status){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setCancelable(status);
        alertDialog.setTitle(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();
    }
}
