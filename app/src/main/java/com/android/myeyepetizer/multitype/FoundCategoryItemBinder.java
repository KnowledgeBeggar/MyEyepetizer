package com.android.myeyepetizer.multitype;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.myeyepetizer.CategoryDetailActivity;
import com.android.myeyepetizer.R;
import com.android.myeyepetizer.adpter.FragmentAdapter;
import com.android.myeyepetizer.gson.Item;
import com.android.myeyepetizer.view.CategoryCollectionItemFragment;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by Administrator on 2017/9/11.
 */

public class FoundCategoryItemBinder extends ItemViewBinder<FoundCategoryItem, FoundCategoryItemBinder.ViewHolder> {

    private Activity mActivity;
    private FragmentManager mManager;

    public FoundCategoryItemBinder(Activity activity) {
        mActivity = activity;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_found_category, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final FoundCategoryItem item) {
        holder.clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CategoryDetailActivity.class);
                intent.putExtra(CategoryDetailActivity.INTENT_ID_KEY, item.data.header.id);

                mActivity.startActivity(intent);
            }
        });

        holder.title.setText(item.data.header.title);
        holder.title.setTypeface(Typeface.createFromAsset(mActivity.getAssets(), mActivity.getString(R.string.DB1FontPath)));

        holder.subtitle.setText(item.data.header.subTitle);
        holder.subtitle.setTypeface(Typeface.createFromAsset(mActivity.getAssets(), mActivity.getString(R.string.LFontPath)));

        holder.addItem(item.data.itemList);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private MultiTypeAdapter mAdapter;
        private Items mItems;
        FrameLayout clickButton;
        TextView title;
        TextView subtitle;
        RecyclerView mRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            clickButton = (FrameLayout) itemView.findViewById(R.id.click_button);
            title = (TextView) itemView.findViewById(R.id.title);
            subtitle = (TextView) itemView.findViewById(R.id.sub_title);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.found_category_item_recycler_view);
            initRecycler();
        }

        private void initRecycler() {
            mAdapter = new MultiTypeAdapter();
            mAdapter.register(Banner.class, new FoundCategoryRecyclerItemBinder(mActivity));


            mItems = new Items();
            mAdapter.setItems(mItems);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(mAdapter);
        }

        public void addItem(List<Item> itemList) {
            mItems.clear();
            for (Item item : itemList) {
                mItems.add(new Banner(item.data));
            }
            mAdapter.notifyDataSetChanged();
        }
    }
}
