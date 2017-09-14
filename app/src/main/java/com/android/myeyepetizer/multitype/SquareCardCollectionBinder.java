package com.android.myeyepetizer.multitype;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.myeyepetizer.R;
import com.android.myeyepetizer.RecyclerViewActivity;
import com.android.myeyepetizer.gson.Item;
import com.android.myeyepetizer.utils.AddSpaceToString;

import java.util.List;

import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by Administrator on 2017/9/7.
 */

public class SquareCardCollectionBinder extends ItemViewBinder<CollectionItem, SquareCardCollectionBinder.ViewHolder> {

    private Activity mActivity;

    public SquareCardCollectionBinder(Activity activity) {
        mActivity = activity;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_collection_square_card_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull CollectionItem item) {
        holder.title.setText(AddSpaceToString.addSpace(item.data.header.title));
        holder.title.setTypeface(Typeface.createFromAsset(mActivity.getAssets(), mActivity.getString(R.string.DB1FontPath)));
        holder.setItems(item.data.itemList, item.intentFlag);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private MultiTypeAdapter mAdapter;
        private Items mItems;
        TextView title;
        RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.square_collection_title);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.square_card_recycler_view);
            initRecycler();
        }

        public void initRecycler() {
            mAdapter = new MultiTypeAdapter();
            mAdapter.register(Banner.class, new SquareCardBinder(mActivity));
            mAdapter.register(Line.class, new VerticalLineBinder());

            mItems = new Items();
            mAdapter.setItems(mItems);
            LinearLayoutManager manager = new LinearLayoutManager(mActivity);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(mAdapter);
        }

        public void setItems(List<Item> items, int flag) {
            mItems.clear();
            mItems.add(new Line());
            for (Item item : items) {
                if (item.type.equals("squareCard")) {
                    mItems.add(new Line());
                    mItems.add(new Banner(item.data, flag));
                } else if (item.type.equals("actionCard")) {
                    if (flag == SquareCardBinder.FLAG_START_CATEGORY_ACTIVITY) {
                        mItems.add(new Line());
                        mItems.add(new Banner(item.data, flag));
                    }
                }
            }
            mItems.add(new Line());
            mAdapter.notifyDataSetChanged();
        }
    }

}
