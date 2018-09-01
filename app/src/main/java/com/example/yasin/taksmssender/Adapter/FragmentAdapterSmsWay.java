package com.example.yasin.taksmssender.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.yasin.taksmssender.Fragment.GroupSendFragment;
import com.example.yasin.taksmssender.Fragment.SoloSendFragment;

public class FragmentAdapterSmsWay extends FragmentPagerAdapter {

    public FragmentAdapterSmsWay(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                SoloSendFragment fragment = new SoloSendFragment();
                return fragment;
            case 1:
                GroupSendFragment fragment1 = new GroupSendFragment();
                return fragment1;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
