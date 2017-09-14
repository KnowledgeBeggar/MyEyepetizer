package com.android.myeyepetizer.multitype;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.myeyepetizer.R;
import com.android.myeyepetizer.utils.TranslateDuration;
import com.bumptech.glide.Glide;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Administrator on 2017/9/8.
 */

public class SubjectItemBinder extends ItemViewBinder<VideoItem, SubjectItemBinder.ViewHolder> {

    private Activity mActivity;

    public SubjectItemBinder(Activity activity) {
        mActivity = activity;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_subject_video, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull VideoItem item) {
        ImageView imageView = new ImageView(mActivity);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(mActivity).load(item.data.cover.detailCover).into(imageView);
        holder.videoView.setThumbImageView(imageView);
        holder.videoView.setUp(item.data.playUrl, true, null);

        holder.title.setText(item.data.title);
        holder.title.setTypeface(Typeface.createFromAsset(mActivity.getAssets(), mActivity.getString(R.string.DB1FontPath)));

        holder.info.setText("#" + item.data.category + "  /  " + TranslateDuration.translateSeconds(item.data.duration));
        holder.info.setTypeface(Typeface.createFromAsset(mActivity.getAssets(), mActivity.getString(R.string.LFontPath)));

        holder.description.setText(item.data.description);
        holder.description.setTypeface(Typeface.createFromAsset(mActivity.getAssets(), mActivity.getString(R.string.LFontPath)));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        StandardGSYVideoPlayer videoView;
        TextView title;
        TextView info;
        TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
            videoView = (StandardGSYVideoPlayer) itemView.findViewById(R.id.video_view);
            title = (TextView) itemView.findViewById(R.id.subject_item_title);
            info = (TextView) itemView.findViewById(R.id.info);
            description = (TextView)itemView.findViewById(R.id.text);
        }
    }

}
