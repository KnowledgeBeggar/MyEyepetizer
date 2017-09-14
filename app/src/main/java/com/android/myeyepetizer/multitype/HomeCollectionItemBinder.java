package com.android.myeyepetizer.multitype;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.myeyepetizer.MovieDetailActivity;
import com.android.myeyepetizer.R;
import com.android.myeyepetizer.utils.TranslateDuration;
import com.bumptech.glide.Glide;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Administrator on 2017/9/6.
 */

public class HomeCollectionItemBinder extends ItemViewBinder<CollectionItem, HomeCollectionItemBinder.ViewHold> {

    private Activity mActivity;

    public HomeCollectionItemBinder(Activity activity) {
        mActivity = activity;
    }

    @NonNull
    @Override
    protected ViewHold onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_home_collection_child_item, parent, false);

        return new ViewHold(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHold holder, @NonNull final CollectionItem item) {
        Glide.with(mActivity).load(item.data.cover.feedCover).into(holder.image);

        holder.title.setText(item.data.title);
        holder.title.setTypeface(Typeface.createFromAsset(mActivity.getAssets(), mActivity.getString(R.string.DB1FontPath)));

        holder.info.setText("#" + item.data.category + " / " + TranslateDuration.translateSeconds(item.data.duration) + " / 开眼精选");
        holder.info.setTypeface(Typeface.createFromAsset(mActivity.getAssets(), mActivity.getString(R.string.LFontPath)));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, MovieDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("DATA", item.data);
                intent.putExtras(bundle);

                mActivity.startActivity(intent);
            }
        });
    }

    class ViewHold extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        TextView info;

        public ViewHold(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.home_item_image);
            image.setClickable(false);
            title = (TextView) itemView.findViewById(R.id.collection_item_title);
            info = (TextView) itemView.findViewById(R.id.collection_item_info);
        }
    }

}
