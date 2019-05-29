package com.example.mysendikapp.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mysendikapp.optionMenuActivities.ActivityAboutUs;
import com.example.mysendikapp.optionMenuActivities.ActivityBoardMembers;
import com.example.mysendikapp.optionMenuActivities.ActivityBranches;
import com.example.mysendikapp.ActivityClaimComplaint;
import com.example.mysendikapp.optionMenuActivities.ActivityContactUs;
import com.example.mysendikapp.R;
import com.example.mysendikapp.haberler.*;
import com.example.mysendikapp.sosyal.*;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Util;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;


public class ActivityDashboard extends AppCompatActivity  {

    private static final String DEBUG_TAG = "sout";
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static String[] haber_id;

    private String[] urls;
    SharedPreferences sp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initImages();

        ActionBar mActionBar = getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View actionBar = mInflater.inflate(R.layout.actionbar_act, null);
        TextView mTitleTextView = (TextView) actionBar.findViewById(R.id.title_text);
        mTitleTextView.setText(R.string.app_name);
        mActionBar.setCustomView(actionBar);
        mActionBar.setDisplayShowCustomEnabled(true);
        ((Toolbar) actionBar.getParent()).setContentInsetsAbsolute(0, 0);

        BoomMenuButton leftBmb = (BoomMenuButton) actionBar.findViewById(R.id.action_bar_left_bmb);
        BoomMenuButton rightBmb = (BoomMenuButton) actionBar.findViewById(R.id.action_bar_right_bmb);

        this.setOptionMenuBMB(leftBmb);
        this.setOptionMenuBMB(rightBmb);

        setMenuActivites();



    }



    //Initialize the slider images
    private void initImages() {
        setUrls();
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(ActivityDashboard.this, urls));

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;
//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES = urls.length;
        this.haber_id = new String[NUM_PAGES];
        setHaber_id();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int pos) {
            }
        });
    }
    private void setUrls() {
        this.urls = new String[]{"https://demonuts.com/Demonuts/SampleImages/W-03.JPG",
                "https://demonuts.com/Demonuts/SampleImages/W-08.JPG",
                "https://demonuts.com/Demonuts/SampleImages/W-10.JPG",
                "https://demonuts.com/Demonuts/SampleImages/W-13.JPG",
                "https://demonuts.com/Demonuts/SampleImages/W-17.JPG",
                "https://demonuts.com/Demonuts/SampleImages/W-21.JPG"};
    }
    public static void setHaber_id() {
        for (int i = 0; i < haber_id.length; i++) {
            ActivityDashboard.haber_id[i] = String.valueOf(i);
        }

    }
    public void ImageOnClick(View v) {
        Toasty.info(ActivityDashboard.this, "PAGE : " + currentPage, Toasty.LENGTH_SHORT).show();
        Intent i = new Intent(ActivityDashboard.this, haberDetaylari.class);
        i.putExtra("haber_id", "" + ActivityDashboard.haber_id[currentPage]);
        startActivity(i);
    }
    //Options menu
    public void setOptionMenuBMB(BoomMenuButton bmb) {
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {

            String buttonNormalText = "", buttonHighlightedText = "";
            int bgID = R.drawable.ic_launcher_background;


            switch (i) {
                case 0:
                    buttonNormalText = "Hakkında";
                    buttonHighlightedText = "Hakkında";
                    bgID = R.drawable.optionaboutus;
                    break;
                case 1:
                    buttonNormalText = "Yönetim";
                    buttonHighlightedText = "Yönetim";
                    bgID = R.drawable.optionboardmembers;
                    break;
                case 2:
                    buttonNormalText = "Bize ulaşın";
                    buttonHighlightedText = "Bize ulaşın";
                    bgID = R.drawable.optioncontact;
                    break;
                case 3:
                    buttonNormalText = "Şubelerimiz";
                    buttonHighlightedText = "Şubelerimiz";
                    bgID = R.drawable.optionbranches;
                    break;

            }
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.

                            switch (index) {
                                case 0: //Hakkında
                                    Toasty.info(ActivityDashboard.this, "Hakkında", Toast.LENGTH_SHORT).show();
                                    Intent i0 = new Intent(ActivityDashboard.this, ActivityAboutUs.class);
                                    startActivity(i0);
                                    break;
                                case 1: //Yönetim
                                    Toasty.info(ActivityDashboard.this, "Yönetim" + index, Toast.LENGTH_SHORT).show();
                                    Intent i1 = new Intent(ActivityDashboard.this, ActivityBoardMembers.class);
                                    startActivity(i1);
                                    break;
                                case 2: //Bize ulaşın
                                    Toasty.info(ActivityDashboard.this, "Bize ulaşın " + index, Toast.LENGTH_SHORT).show();
                                    Intent i2 = new Intent(ActivityDashboard.this, ActivityContactUs.class);
                                    startActivity(i2);
                                    break;
                                case 3: //Şubelerimiz
                                    Toasty.info(ActivityDashboard.this, "Şubelerimiz " + index, Toast.LENGTH_SHORT).show();
                                    Intent i3 = new Intent(ActivityDashboard.this, ActivityBranches.class);
                                    startActivity(i3);
                                    break;
                            }
                        }
                    })
                    .shadowOffsetX(40)
                    .shadowOffsetY(40)


                    // Set the image resource when boom-button is at normal-state.
                    .normalImageRes(bgID)
                    // Set the image resource when boom-button is at highlighted-state.
                    .highlightedImageRes(R.drawable.piece_dot)
                    // Set the image resource when boom-button is at unable-state.
                    .unableImageRes(R.drawable.piece)
                    // Set the padding of image.
                    // By this method, you can control the padding in the image-view.
                    // For instance, builder.imagePadding(new Rect(10, 10, 10, 10)) will make the
                    // image-view content 10-pixel padding to itself.
                    .imagePadding(new Rect(2, 2, 2, 2))


                    // Set the text when boom-button is at normal-state.
                    .normalText(buttonNormalText)
                    // Set the text when boom-button is at highlighted-state.
                    .highlightedText(buttonHighlightedText)
                    // Set the text when boom-button is at unable-state.
                    .unableText("Unable!")
                    // Set the color of text when boom-button is at normal-state.
                    .normalTextColor(Color.WHITE)
                    // Set the color of text when boom-button is at highlighted-state.
                    .highlightedTextColor(Color.BLUE)
                    // Set the color of text when boom-button is at unable-state.
                    .unableTextColor(Color.RED)
                    // Set the padding of text.
                    // By this method, you can control the padding in the text-view.
                    // For instance, builder.textPadding(new Rect(10, 10, 10, 10)) will make the
                    // text-view content 10-pixel padding to itself.
                    .textPadding(new Rect(0, 0, 0, 0))
                    // Set the gravity of text-view.
                    // Set the text size of the text-view.
                    .textSize(15)
                    // The color of boom-button when it is at normal-state.
                    .normalColor(Color.WHITE)
                    // The color of boom-button when it is at highlighted-state.
                    .highlightedColor(Color.BLUE)
                    // The color of boom-button when it is at unable-state.
                    .unableColor(Color.BLACK)

                    // The radius of boom-button, in pixel.
                    .buttonRadius(Util.dp2px(40))
                    // Whether the button is a circle shape.
                    .isRound(true);
            bmb.addBuilder(builder);
        }
    }
    //Main menu
    public void setMenuActivites() {

        //Facebook
        findViewById(R.id.imageViewFacebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, ActivityFacebook.class);
                startActivity(i);
            }
        });
        //Facebook\\


        //Twitter
        findViewById(R.id.imageViewTwitter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, ActivityTwitter.class);
                startActivity(i);
            }
        });
        //Twitter\\


        //Haberler
        findViewById(R.id.btnHaberleriGor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, newsFeed.class);
                startActivity(i);
            }
        });
        //Haberler\\

        //ClaimComplaint
        findViewById(R.id.imageViewTalep).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, ActivityClaimComplaint.class);
                startActivity(i);
            }
        });
        //ClaimComplaint\\


        //Menu clickable images things\\
    }

    @Override
    public void onBackPressed() {
    } //Do Nothing
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is presents
        getMenuInflater().inflate(R.menu.menu_topmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_icon:
                this.logout();
        }
        return true;
    }

    public void logout() {
        Log.d("logout Function", "Logout function");

        sp = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("isLogged");
        editor.remove("username");
        editor.remove("password");
        editor.putBoolean("isLogged", false);
        editor.apply();

        Intent i = new Intent(this, com.example.mysendikapp.login.loginActivity.class);
        startActivity(i);
    }

    //  OVERRIDE METHODS NOT USED \\
    //  OVERRIDE METHODS NOT USED \\
    //  OVERRIDE METHODS NOT USED \\



}






