package com.android.myeyepetizer.adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class FragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> mTitles;

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        if (fragments != null) {
            mFragments = fragments;
        }
        mTitles = titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles != null) {
            return mTitles.get(position);
        } else {
            return null;
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    public void setFragments(List<Fragment> fragments) {
        mFragments = fragments;
    }
}
