package com.example.mysendikapp.login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {
    private int numOfTabs;
    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    public PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new fragmentGirisYap();
//            case 1:
//                return new fragmentKayitOL();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return this.numOfTabs;
    }

}
