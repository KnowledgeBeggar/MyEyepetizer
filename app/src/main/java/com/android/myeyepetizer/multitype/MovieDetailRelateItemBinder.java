package com.android.myeyepetizer.multitype;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.myeyepetizer.MovieDetailActivity;
import com.android.myeyepetizer.R;
import com.android.myeyepetizer.utils.TranslateDuration;
import com.bumptech.glide.Glide;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Administrator on 2017/9/4.
 */

public class MovieDetailRelateItemBinder extends ItemViewBinder<MovieDetailRelateItem, MovieDetailRelateItemBinder.ViewHolder> {

    public static final int FLAG_WHITE_WORD_COLOR = 0;
    public static final int FLAG_BLACK_WORD_COLOR = 1;

    private Activity mActivity;
    private int mFlag;

    public MovieDetailRelateItemBinder(Activity activity, int flag) {
        super();
        mActivity = activity;
        mFlag = flag;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_movie_relate, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final MovieDetailRelateItem item) {
        Glide.with(mActivity).load(item.data.cover.feedCover).into(holder.coverImage);
        holder.title.setText(item.data.title);
        holder.relateMovieInfo.setText("#" + item.data.category + " / " + TranslateDuration.translateSeconds(item.data.duration));
        if (mFlag == FLAG_BLACK_WORD_COLOR){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, MovieDetailActivity.class);
                    intent.putExtra("DATA", item.data);
                    mActivity.startActivity(intent);
                }
            });
        } else if (mFlag == FLAG_WHITE_WORD_COLOR) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MovieDetailActivity activity = (MovieDetailActivity) mActivity;
                    activity.updateData(item.data);
                }
            });
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        FrameLayout background;
        ImageView coverImage;
        TextView title;
        TextView relateMovieInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            background = (FrameLayout) itemView.findViewById(R.id.background);
            coverImage = (ImageView) itemView.findViewById(R.id.relate_cover);
            title = (TextView) itemView.findViewById(R.id.relate_title);
            title.setTypeface(Typeface.createFromAsset(mActivity.getAssets(), mActivity.getResources().getString(R.string.DB1FontPath)));
            relateMovieInfo = (TextView) itemView.findViewById(R.id.relate_info);
            relateMovieInfo.setTypeface(Typeface.createFromAsset(mActivity.getAssets(),
                    mActivity.getResources().getString(R.string.LFontPath)));

            if (mFlag == FLAG_BLACK_WORD_COLOR) {
                background.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorWhite));
                title.setTextColor(ContextCompat.getColor(mActivity, R.color.colorBlack));
                relateMovieInfo.setTextColor(ContextCompat.getColor(mActivity, R.color.colorBlack));
            }
        }
    }

}
