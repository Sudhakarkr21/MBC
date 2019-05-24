package com.example.mbc_tvd;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mbc_tvd.Fragments.SendSubdivCode;
import com.example.mbc_tvd.Others.RoundedImageView;

import static com.example.mbc_tvd.Others.Constant.LAYOUT;
import static com.example.mbc_tvd.Others.Constant.PREF_NAME;
import static com.example.mbc_tvd.Others.Constant.sPref_ROLE;
import static com.example.mbc_tvd.Others.Constant.sPref_SUBDIVISION;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences sPref;
    SharedPreferences.Editor editor;
    String SubdivCode,currentversion = "0";
    private Fragment fragment;
    private Toolbar toolbar;
    FragmentManager fragmentManager;
    RoundedImageView img_profile;
    BroadcastReceiver br;
    TextView textView;
    NavigationView navigationView;
    private boolean isFirstBackPressed = false;
    public enum Steps {
        FORM2(SendSubdivCode.class);
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
        setContentView(R.layout.activity_main);
        sPref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = sPref.edit();
        editor.commit();

        fragmentManager = getSupportFragmentManager();
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkInternetConnection();

        Intent intent = getIntent();
        SubdivCode = intent.getStringExtra("subdivcode");
        currentversion = intent.getStringExtra("versioncode");
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        HeaderView();




        NavigationView logout_navigationView =  findViewById(R.id.nav_drawer_bottom);
        logout_navigationView.setNavigationItemSelectedListener(this);
        logout_navigationView.setItemTextColor(ColorStateList.valueOf(Color.parseColor("#039BE5")));

        Menu menu = logout_navigationView.getMenu();

        MenuItem nav_version = menu.findItem(R.id.nav_versioncode);
        nav_version.setTitle("Version :"+currentversion);

        switchContent(Steps.FORM2, getResources().getString(R.string.login));
    }
        public void HeaderView(){
            View mHaderView = navigationView.getHeaderView(0);
            textView = mHaderView.findViewById(R.id.user_name);
            textView.setText(SubdivCode);
            img_profile = mHaderView.findViewById(R.id.img_profile);
            Glide.with(this).load(Uri.parse("https://imgur.com/rXkY3JL.jpg")).into(img_profile);
        }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0){
            super.onBackPressed();
        }else{
            if (isFirstBackPressed) {
                super.onBackPressed();
            } else {
                isFirstBackPressed = true;
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isFirstBackPressed = false;
                    }
                }, 2000);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.main:
                switchContent(Steps.FORM2, getResources().getString(R.string.login));
                break;


            case R.id.logout_nav:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                editor.putString(sPref_ROLE,"");
                editor.putString(sPref_SUBDIVISION,"");
                editor.clear();
                editor.commit();
                finish();
                break;
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void switchContent(Steps currentForm, String title) {
        try {
            fragment = (Fragment) currentForm.getFragClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        toolbar.setTitle(title);
        ft.replace(R.id.container_navigation, fragment, currentForm.name());
        ft.commit();
    }

    public void startIntent(Context context, String layout) {
        Intent intent = new Intent(context, Main_Activity2.class);
        intent.putExtra(LAYOUT, layout);
        startActivity(intent);
    }

    private void checkInternetConnection() {

        if (br == null) {

            br = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {

                    Bundle extras = intent.getExtras();

                    NetworkInfo info = (NetworkInfo) extras
                            .getParcelable("networkInfo");

                    NetworkInfo.State state = info.getState();
                    Log.d("TEST Internet", info.toString() + " "
                            + state.toString());

                    if (state == NetworkInfo.State.CONNECTED) {
                        toastmessage(getResources().getColor(R.color.green),"Online");
//                        Toast.makeText(getApplicationContext(), "Internet connection is on", Toast.LENGTH_LONG).show();

                    } else {
                        toastmessage(getResources().getColor(R.color.red),"No internet connection");
//                        Toast.makeText(getApplicationContext(), "Internet connection is Off", Toast.LENGTH_LONG).show();
                    }

                }
            };

            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver( br, intentFilter);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

    public void toastmessage(int a,String message){
        @SuppressLint("InflateParams") View inflater = getLayoutInflater().inflate(R.layout.toast_layout,null);

        Toast toast = new Toast(getApplicationContext());

        ImageView imageView = inflater.findViewById(R.id.image);
        imageView.setVisibility(View.GONE);
        LinearLayout linearLayout = inflater.findViewById(R.id.toast_layout);
        linearLayout.setBackgroundColor(a);
        TextView textView = inflater.findViewById(R.id.text1);
        textView.setText(message);
        textView.setTextColor(getResources().getColor(R.color.white));

        toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(inflater);
        toast.show();
    }
}
