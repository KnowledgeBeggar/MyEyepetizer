package com.android.myeyepetizer.multitype;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.android.myeyepetizer.R;
import com.android.myeyepetizer.RecyclerViewActivity;
import com.android.myeyepetizer.SubjectActivity;
import com.bumptech.glide.Glide;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Administrator on 2017/9/5.
 */

public class BannerBinder extends ItemViewBinder<Banner, BannerBinder.ViewHolder> {

    private Activity mActivity;

    public BannerBinder(Activity activity) {
        mActivity = activity;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_banner, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Banner item) {
        Glide.with(mActivity).load(item.data.image).into(holder.imageButton);
        if (mActivity instanceof RecyclerViewActivity) {
            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, SubjectActivity.class);
                    intent.putExtra("ID", item.data.id);
                    mActivity.startActivity(intent);
                }
            });
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton imageButton;

        public ViewHolder(View itemView) {
            super(itemView);
            imageButton = (ImageButton) itemView.findViewById(R.id.home_item_image);
        }
    }

}
