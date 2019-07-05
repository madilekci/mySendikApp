package com.example.mysendikapp.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.mysendikapp.R;

public class loginActivity extends AppCompatActivity {
    String TAG="loginActivity(NotFragment)";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TabLayout tabLayout = findViewById(R.id.tablayout_login);
        final ViewPager viewPager = findViewById(R.id.viewPager_login);
        final PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0)
                    viewPager.setCurrentItem(0);
//                if (tab.getPosition() == 1)
//                    viewPager.setCurrentItem(1);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }


        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }



    public static boolean checkConditions(Context ctx){
        // get Internet status
        boolean isInternetPresent = isConnectingToInternet(ctx);

        if (!isInternetPresent) {
            showAlertDialog(ctx, "İnternet bağlantınız yok", "Lütfen internet bağlantınızı kontrol edip yeniden deneyin", false);
        }

        return isInternetPresent;
    }

    public static boolean isConnectingToInternet(Context ctx) {
        ConnectivityManager connectivity = (ConnectivityManager) ctx.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }
    public static void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting try again Button
        alertDialog.setButton(Dialog.BUTTON_POSITIVE,"Tekrar bağlanmayı dene",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                killApp();
            }
        });



        // Showing Alert Message
        alertDialog.show();
    }
    public static void killApp(){
        Log.d("checkConditions","quit ...");
            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
            System.exit(0);

    }
}
