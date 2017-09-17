package com.android.myeyepetizer;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.android.myeyepetizer.view.FollowFragment;
import com.android.myeyepetizer.view.FoundFragment;
import com.android.myeyepetizer.view.HomeFragment;
import com.android.myeyepetizer.view.MineFragment;
import com.android.myeyepetizer.view.MyRadioButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{

    private static final int HOME_RADIO_BUTTON_INDEX = 0;
    private static final int FOUND_RADIO_BUTTON_INDEX = 1;
    private static final int FOLLOW_RADIO_BUTTON_INDEX = 2;
    private static final int MINE_RADIO_BUTTON_INDEX = 3;

    private RadioGroup mRadioGroup;

    private FragmentManager mFragmentManager;
    private List<Fragment> mFragmentList = new ArrayList<>();

    private int mCurrentRB = HOME_RADIO_BUTTON_INDEX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRadioButton();
        mRadioGroup.setOnCheckedChangeListener(this);

        initFragments();
        mFragmentManager = getSupportFragmentManager();
        Fragment fragment = mFragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = mFragmentList.get(HOME_RADIO_BUTTON_INDEX);
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

    }

    private void initRadioButton() {
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
    }

    private void initFragments() {
        mFragmentList.add(HomeFragment.newInstance());
        mFragmentList.add(FoundFragment.newInstance());
        mFragmentList.add(FollowFragment.newInstance());
        mFragmentList.add(MineFragment.newInstance());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.home_radio:
                changeFragment(HOME_RADIO_BUTTON_INDEX);
                break;
            case R.id.found_radio:
                changeFragment(FOUND_RADIO_BUTTON_INDEX);
                break;
            case R.id.follow_radio:
                changeFragment(FOLLOW_RADIO_BUTTON_INDEX);
                break;
            case R.id.mine_radio:
                changeFragment(MINE_RADIO_BUTTON_INDEX);
                break;
            default:
                break;
        }
    }

    private void changeFragment(final int index) {
        if (mFragmentList.get(index).isAdded()) {
            mFragmentManager.beginTransaction()
                    .hide(mFragmentList.get(mCurrentRB))
                    .show(mFragmentList.get(index))
                    .commit();
        } else {
            mFragmentManager.beginTransaction()
                    .hide(mFragmentList.get(mCurrentRB))
                    .add(R.id.fragment_container, mFragmentList.get(index))
                    .commit();
        }
        mCurrentRB = index;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
