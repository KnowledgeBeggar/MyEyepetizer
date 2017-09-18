package com.android.myeyepetizer.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.android.myeyepetizer.MovieDetailActivity;
import com.android.myeyepetizer.R;
import com.android.myeyepetizer.gson.Data;
import com.bumptech.glide.Glide;

import java.util.List;

public class HomeTopPagerFragment extends Fragment {

    public TyperTextView mTitleView;
    public TyperTextView mSloganView;
    public ImageButton imageButton;

    private String mImageUri;
    private String mTitle;
    private String mSlogan;
    private Data mData;

    public static HomeTopPagerFragment newInstance(Data data) {
        HomeTopPagerFragment fragment = new HomeTopPagerFragment();
        fragment.setData(data);
        fragment.setImageUri(data.cover.homePageCover);
        fragment.setTitle(data.title);
        fragment.setSlogan(data.slogan);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_top_pager, container, false);

        imageButton = (ImageButton) view.findViewById(R.id.image_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("DATA", mData);
                intent.putExtras(bundle);

                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.fake_anim);
            }
        });
        Glide.with(getActivity()).load(mImageUri).into(imageButton);

        mTitleView = (TyperTextView) view.findViewById(R.id.ttv);
        mTitleView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), getActivity().getString(R.string.DB1FontPath)));
        mTitleView
                .setAnimationString(mTitle)
                .startAnimation();

        mSloganView = (TyperTextView) view.findViewById(R.id.ttv1);
        mSloganView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), getActivity().getString(R.string.LFontPath)));
        mSloganView
                .setAnimationString(mSlogan)
                .startAnimation();

        return view;
    }

    public void setImageUri(String uri) {
        mImageUri = uri;
    }

    public String getImageUri() {
        return mImageUri;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSlogan() {
        return mSlogan;
    }

    public void setSlogan(String slogan) {
        mSlogan = slogan;
    }

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
        mData = data;
    }
}
