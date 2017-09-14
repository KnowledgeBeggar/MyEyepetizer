package com.android.myeyepetizer.multitype;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.android.myeyepetizer.R;
import com.android.myeyepetizer.SearchActivity;
import com.android.myeyepetizer.adpter.FragmentAdapter;
import com.android.myeyepetizer.view.HomeTopPagerFragment;
import com.android.myeyepetizer.view.TyperTextView;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Administrator on 2017/8/25.
 */

public class HomeTopPagerBinder extends ItemViewBinder<HomeTopPager, HomeTopPagerBinder.ViewHolder> {


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_home_top_pager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final HomeTopPager item) {
        final List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < item.dataList.size(); i++) {
            HomeTopPagerFragment homePagerFragment = HomeTopPagerFragment.newInstance(item.dataList.get(i));
            fragments.add(homePagerFragment);
        }
        holder.mViewPager.setAdapter(new FragmentAdapter(item.manager, fragments, null));
        holder.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                TyperTextView t1 = ((HomeTopPagerFragment) fragments.get(position)).mTitleView;
                TyperTextView t2 = ((HomeTopPagerFragment) fragments.get(position)).mSloganView;
                if (t1 != null && t2 != null) {
                    t1.startAnimation();
                    t2.startAnimation();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        holder.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(item.activity, SearchActivity.class);
                item.activity.startActivity(intent);
                item.activity.overridePendingTransition(R.anim.slide_in_up, R.anim.fake_anim);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ViewPager mViewPager;
        ImageButton searchButton;
        FrameLayout mLayout;

        public ViewHolder(final View itemView) {
            super(itemView);
            mViewPager = (ViewPager) itemView.findViewById(R.id.view_pager);
            searchButton = (ImageButton) itemView.findViewById(R.id.search_button);
            mLayout = (FrameLayout) itemView.findViewById(R.id.search_layout);
        }
    }

}
