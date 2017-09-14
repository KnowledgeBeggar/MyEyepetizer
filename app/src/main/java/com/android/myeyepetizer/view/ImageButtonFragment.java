package com.android.myeyepetizer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.android.myeyepetizer.R;
import com.android.myeyepetizer.gson.Data;
import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2017/9/9.
 */

public class ImageButtonFragment extends Fragment {

    public static ImageButtonFragment newInstance(Data data) {
        ImageButtonFragment fragment = new ImageButtonFragment();
        fragment.setData(data);
        return fragment;
    }

    private ImageButton coverButton;
    private Data mData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_image_view, container, false);
        coverButton = (ImageButton) view.findViewById(R.id.home_item_image);
        Glide.with(getActivity()).load(mData.image).into(coverButton);
        return view;
    }

    public void setData(Data data) {
        mData = data;
    }
}
