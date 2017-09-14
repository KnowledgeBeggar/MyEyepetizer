package com.android.myeyepetizer.multitype;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.myeyepetizer.R;
import com.android.myeyepetizer.adpter.FragmentAdapter;
import com.android.myeyepetizer.gson.Item;
import com.android.myeyepetizer.view.CategoryCollectionItemFragment;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Administrator on 2017/9/12.
 */

public class FoundCategoryDetailCollectionBinder extends ItemViewBinder<FoundCategoryDetailCollection, FoundCategoryDetailCollectionBinder.ViewHolder> {

    private FragmentManager mManager;
    private Activity mActivity;

    public FoundCategoryDetailCollectionBinder(FragmentManager manager, Activity activity) {
        mManager = manager;
        mActivity = activity;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_found_category_detail_collection, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull FoundCategoryDetailCollection item) {
        holder.title.setText(item.data.header.title);
        holder.title.setTypeface(Typeface.createFromAsset(mActivity.getAssets(), mActivity.getString(R.string.DB1FontPath)));

        List<Fragment> fragments = new ArrayList<>();
        for (Item item1 : item.data.itemList) {
            CategoryCollectionItemFragment fragment = CategoryCollectionItemFragment.newInstance(item1.data);
            fragments.add(fragment);
        }
        holder.setFragments(fragments);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private FragmentAdapter mAdapter;
        TextView title;
        ViewPager viewPager;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            viewPager = (ViewPager) itemView.findViewById(R.id.found_category_detail_view_pager);
            mAdapter = new FragmentAdapter(mManager, null, null);
            viewPager.setAdapter(mAdapter);
        }

        public void setFragments(List<Fragment> fragments) {
            mAdapter.setFragments(fragments);
            mAdapter.notifyDataSetChanged();
        }
    }

}
