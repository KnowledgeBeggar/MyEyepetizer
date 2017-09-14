package com.android.myeyepetizer.multitype;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.myeyepetizer.MovieDetailActivity;
import com.android.myeyepetizer.R;
import com.android.myeyepetizer.utils.TranslateDuration;
import com.bumptech.glide.Glide;

import me.drakeet.multitype.ItemViewBinder;

public class HomeItemBinder extends ItemViewBinder<HomeItem, HomeItemBinder.ViewHolder> {

    private Activity mActivity;

    public HomeItemBinder(Activity activity) {
        super();
        mActivity = activity;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_home, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final HomeItem item) {
        Context context = holder.imageButton.getContext();
        Glide.with(context).load(item.data.cover.feedCover).into(holder.imageButton);
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

        String icon;
        if (item.data.author != null) {
            icon = item.data.author.icon;
        } else {
            icon = item.data.provider.icon;
        }
        if (icon.equals("")) {
            holder.authorIcon.setImageResource(R.drawable.ic_launcher);
        } else {
            Glide.with(context).load(icon).into(holder.authorIcon);
        }

        holder.title.setText(item.data.title);
        holder.title.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getString(R.string.DB1FontPath)));
        holder.info.setText("#" + item.data.category + " / " + TranslateDuration.translateSeconds(item.data.duration));
        holder.info.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getString(R.string.LFontPath)));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton imageButton;
        ImageView authorIcon;
        TextView title;
        TextView info;

        public ViewHolder(View itemView) {
            super(itemView);
            imageButton = (ImageButton) itemView.findViewById(R.id.home_item_image);
            authorIcon = (ImageView) itemView.findViewById(R.id.author_icon);
            title = (TextView) itemView.findViewById(R.id.movie_title);
            info = (TextView) itemView.findViewById(R.id.info);
        }
    }

}
