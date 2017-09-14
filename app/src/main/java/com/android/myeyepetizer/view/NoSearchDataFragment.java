package com.android.myeyepetizer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.myeyepetizer.R;

/**
 * Created by Administrator on 2017/9/13.
 */

public class NoSearchDataFragment extends Fragment {

    public static NoSearchDataFragment newInstance() {
        NoSearchDataFragment fragment = new NoSearchDataFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_about_data, container, false);

        return view;
    }
}
