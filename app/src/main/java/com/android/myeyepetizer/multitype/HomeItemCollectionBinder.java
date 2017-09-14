package com.android.myeyepetizer.multitype;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.android.myeyepetizer.R;
import com.android.myeyepetizer.SubjectActivity;
import com.android.myeyepetizer.gson.Item;
import com.bumptech.glide.Glide;

import java.util.List;

import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by Administrator on 2017/9/6.
 */

public class HomeItemCollectionBinder extends ItemViewBinder<HomeItemCollection, HomeItemCollectionBinder.ViewHolder> {

    private Activity mActivity;

    public HomeItemCollectionBinder(Activity activity) {
        mActivity = activity;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_home_collection, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final HomeItemCollection item) {
        Glide.with(mActivity).load(item.data.header.cover).into(holder.imageButton);
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SubjectActivity.class);
                intent.putExtra("ID", item.data.header.id);
                mActivity.startActivity(intent);
            }
        });
        holder.setItems(item.data.itemList);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private Items mItems;
        private MultiTypeAdapter mAdapter;
        ImageButton imageButton;
        RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageButton = (ImageButton) itemView.findViewById(R.id.home_item_image);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.horizontal_recycler_view);
            initRecycler();
        }

        private void initRecycler() {
            mAdapter = new MultiTypeAdapter();
            mAdapter.register(CollectionItem.class, new HomeCollectionItemBinder(mActivity));
            mAdapter.register(Line.class, new VerticalLineBinder());

            mItems = new Items();
            mAdapter.setItems(mItems);
            LinearLayoutManager manager = new LinearLayoutManager(mActivity);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(mAdapter);
        }

        public void setItems(List<Item> items) {
            mItems.clear();
            for (Item item : items) {
                mItems.add(new Line());
                mItems.add(new CollectionItem(item.data));
            }
            mItems.add(new Line());
            mAdapter.notifyDataSetChanged();
        }
    }

}
