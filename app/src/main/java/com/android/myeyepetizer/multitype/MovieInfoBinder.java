package com.android.myeyepetizer.multitype;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.myeyepetizer.R;
import com.android.myeyepetizer.utils.TranslateDuration;
import com.android.myeyepetizer.view.TyperTextView;

import me.drakeet.multitype.ItemViewBinder;


public class MovieInfoBinder extends ItemViewBinder<MovieInfo, MovieInfoBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_movie_info, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull MovieInfo item) {
        Context context = holder.mTitle.getContext();
        holder.mTitle
                .setAnimationString(item.title);
        holder.mTitle.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.DB1FontPath)));

        holder.mMovieInfo
                .setAnimationString("#" + item.category + " / " +TranslateDuration.translateSeconds(item.duration) + " / 开眼精选");
        holder.mMovieInfo.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.LFontPath)));

        holder.mDescription
                .setAnimationString(item.description);
        holder.mDescription.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.LFontPath)));

        if (item.isStartAnimation) {
            holder.mTitle.startAnimation();
            holder.mMovieInfo.startAnimation();
            holder.mDescription.startAnimation();

            item.isStartAnimation = false;
        }

        holder.mCollectionCount.setText(item.collectionCount + "");
        holder.mShareCount.setText(item.shareCount + "");
        holder.mReplyCount.setText(item.replyCount + "");
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TyperTextView mTitle;
        TyperTextView mMovieInfo;
        TyperTextView mDescription;
        TextView mCollectionCount;
        TextView mShareCount;
        TextView mReplyCount;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = (TyperTextView) itemView.findViewById(R.id.title_text);
            mMovieInfo = (TyperTextView) itemView.findViewById(R.id.movie_info);
            mDescription = (TyperTextView) itemView.findViewById(R.id.description);
            mCollectionCount = (TextView) itemView.findViewById(R.id.collection_count);
            mShareCount = (TextView) itemView.findViewById(R.id.share_count);
            mReplyCount = (TextView) itemView.findViewById(R.id.reply_count);
        }

    }

}
