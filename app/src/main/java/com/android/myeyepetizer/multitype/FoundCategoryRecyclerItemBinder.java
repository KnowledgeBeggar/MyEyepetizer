package com.android.myeyepetizer.multitype;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.myeyepetizer.MovieDetailActivity;
import com.android.myeyepetizer.R;
import com.android.myeyepetizer.utils.TranslateDuration;
import com.bumptech.glide.Glide;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Administrator on 2017/9/12.
 */

public class FoundCategoryRecyclerItemBinder extends ItemViewBinder<Banner, FoundCategoryRecyclerItemBinder.ViewHolder> {

    private Activity mActivity;

    public FoundCategoryRecyclerItemBinder(Activity activity) {
        mActivity = activity;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_found_category_child_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Banner item) {
        Context context = holder.imageButton.getContext();
        Glide.with(context).load(item.data.cover.detailCover).into(holder.imageButton);
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, MovieDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("DATA", item.data);
                intent.putExtras(bundle);

                mActivity.startActivity(intent);
            }
        });

        holder.title.setText(item.data.title);
        holder.title.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getString(R.string.DB1FontPath)));

        holder.info.setText("#" + item.data.category + "  /  " + TranslateDuration.translateSeconds(item.data.duration));
        holder.info.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getString(R.string.LFontPath)));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton imageButton;
        TextView title;
        TextView info;

        public ViewHolder(View itemView) {
            super(itemView);
            imageButton = (ImageButton) itemView.findViewById(R.id.image_button);
            title = (TextView) itemView.findViewById(R.id.title);
            info = (TextView) itemView.findViewById(R.id.info);
        }
    }

}
