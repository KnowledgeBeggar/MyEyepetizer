package com.android.myeyepetizer.multitype;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.myeyepetizer.AuthorDetailActivity;
import com.android.myeyepetizer.R;
import com.android.myeyepetizer.gson.Author;
import com.android.myeyepetizer.gson.Item;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by Administrator on 2017/9/12.
 */

public class AuthorCollectionItemBinder extends ItemViewBinder<AuthorCollectionItem, AuthorCollectionItemBinder.ViewHolder> {

    public static final int FLAG_INTENT_AUTHOR = 0;
    public static final int FLAG_INTENT_ALBUM = 1;

    private Activity mActivity;

    public AuthorCollectionItemBinder(Activity activity) {
        mActivity = activity;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_author_collection, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final AuthorCollectionItem item) {
        Glide.with(mActivity).load(item.data.header.icon).into(holder.authorIcon);

        holder.auhtorName.setText(item.data.header.title);
        holder.authorDescription.setText(item.data.header.description);
        if (item.type == FLAG_INTENT_AUTHOR) {
            holder.collection.setTextColor(ContextCompat.getColor(mActivity, R.color.colorBlack));
            holder.collection.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.ic_collection_button_black));
            holder.clickLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, AuthorDetailActivity.class);
                    intent.putExtra(AuthorDetailActivity.FLAG_INTENT_ID, item.data.header.id);
                    mActivity.startActivity(intent);
                }
            });
        } else if (item.type == FLAG_INTENT_ALBUM) {
            holder.collection.setVisibility(View.GONE);
        }
        holder.addItem(item.data.itemList);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private MultiTypeAdapter mAdapter;
        private Items mItems;
        LinearLayout clickLayout;
        CircleImageView authorIcon;
        TextView auhtorName;
        TextView authorDescription;
        RecyclerView recyclerView;
        Button collection;

        public ViewHolder(View itemView) {
            super(itemView);
            clickLayout = (LinearLayout) itemView.findViewById(R.id.click_layout);
            authorIcon = (CircleImageView) itemView.findViewById(R.id.author_icon);

            auhtorName = (TextView) itemView.findViewById(R.id.author_name);
            auhtorName.setTypeface(Typeface.createFromAsset(mActivity.getAssets(), mActivity.getString(R.string.DB1FontPath)));
            auhtorName.setTextColor(ContextCompat.getColor(mActivity, R.color.colorBlack));

            authorDescription = (TextView) itemView.findViewById(R.id.author_description);
            authorDescription.setTypeface(Typeface.createFromAsset(mActivity.getAssets(), mActivity.getString(R.string.LFontPath)));
            authorDescription.setTextColor(ContextCompat.getColor(mActivity, R.color.colorBlack));

            recyclerView = (RecyclerView) itemView.findViewById(R.id.horizontal_recycler_view);
            initRecycler();

            collection = (Button) itemView.findViewById(R.id.collection_button);
        }

        private void initRecycler() {
            mAdapter = new MultiTypeAdapter();
            mAdapter.register(Line.class, new VerticalLineBinder());
            mAdapter.register(CollectionItem.class, new HomeCollectionItemBinder(mActivity));

            mItems = new Items();
            mAdapter.setItems(mItems);
            LinearLayoutManager manager = new LinearLayoutManager(mActivity);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(mAdapter);
        }

        public void addItem(List<Item> itemList) {
            mItems.clear();
            if (itemList != null) {
                mItems.add(new Line());
                mItems.add(new Line());
                for (Item item : itemList) {
                    if (item.type.equals("video")) {
                        mItems.add(new CollectionItem(item.data));
                        mItems.add(new Line());
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    }

}
