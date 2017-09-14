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
import com.android.myeyepetizer.utils.TranslateDateFormat;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Administrator on 2017/9/7.
 */

public class ReplyInfoItemBinder extends ItemViewBinder<ReplyInfoItem, ReplyInfoItemBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_reply_info, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ReplyInfoItem item) {
        Context context = holder.avatar.getContext();
        Glide.with(context).load(item.reply.user.avatar).into(holder.avatar);

        holder.nickname.setText(item.reply.user.nickname);
        holder.nickname.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getString(R.string.DB1FontPath)));

        holder.likeCount.setText(item.reply.likeCount + "");
        holder.likeCount.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getString(R.string.LFontPath)));

        holder.message.setText(item.reply.message);
        holder.message.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getString(R.string.LFontPath)));

        holder.replyDate.setText(TranslateDateFormat.getDate(item.reply.createTime));
        holder.replyDate.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getString(R.string.LFontPath)));

        holder.text.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getString(R.string.LFontPath)));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView avatar;
        TextView nickname;
        TextView likeCount;
        TextView message;
        TextView replyDate;
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            nickname = (TextView) itemView.findViewById(R.id.nickname);
            likeCount = (TextView) itemView.findViewById(R.id.like_count);
            message = (TextView) itemView.findViewById(R.id.message);
            replyDate = (TextView) itemView.findViewById(R.id.reply_date);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }

}
