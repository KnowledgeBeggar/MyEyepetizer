package com.android.myeyepetizer.adpter;

import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/11.
 */

public class ImageButtonAdpter extends PagerAdapter {
    private List<ImageButton> mViewList = new ArrayList<>();

    public ImageButtonAdpter(List<ImageButton> viewList) {
        if (viewList != null) {
            mViewList = viewList;
        }
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViewList.get(position));
        return mViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));
    }

    public void setViewList(List<ImageButton> viewList) {
        mViewList = viewList;
    }
}
