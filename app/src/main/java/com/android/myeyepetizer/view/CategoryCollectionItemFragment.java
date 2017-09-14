package com.android.myeyepetizer.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.myeyepetizer.MovieDetailActivity;
import com.android.myeyepetizer.R;
import com.android.myeyepetizer.gson.Data;
import com.android.myeyepetizer.multitype.Category;
import com.android.myeyepetizer.utils.TranslateDateFormat;
import com.android.myeyepetizer.utils.TranslateDuration;
import com.bumptech.glide.Glide;


/**
 * Created by Administrator on 2017/9/11.
 */

public class CategoryCollectionItemFragment extends Fragment {

    public static CategoryCollectionItemFragment newInstance(Data data) {
        CategoryCollectionItemFragment fragment = new CategoryCollectionItemFragment();
        fragment.setData(data);
        return fragment;
    }

    private ImageButton mImageButton;
    private TextView mTitle;
    private TextView mInfo;
    private Data mData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_found_category_child_layout, container, false);
        if (mData != null) {
            mImageButton = (ImageButton) view.findViewById(R.id.image_button);
            Glide.with(getActivity()).load(mData.cover.detailCover).into(mImageButton);
            mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("DATA", mData);
                    intent.putExtras(bundle);

                    getActivity().startActivity(intent);
                }
            });

            mTitle = (TextView) view.findViewById(R.id.title);
            mTitle.setText(mData.title);
            mTitle.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), getActivity().getString(R.string.DB1FontPath)));

            mInfo = (TextView) view.findViewById(R.id.info);
            mInfo.setText("#" + mData.category + "  /  " + TranslateDuration.translateSeconds(mData.duration));
            mInfo.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), getActivity().getString(R.string.LFontPath)));
        }

        return view;
    }

    public void setData(Data data) {
        mData = data;
    }
}
