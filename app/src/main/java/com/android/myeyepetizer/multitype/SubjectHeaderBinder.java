package com.android.myeyepetizer.multitype;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.myeyepetizer.R;
import com.bumptech.glide.Glide;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Administrator on 2017/9/8.
 */

public class SubjectHeaderBinder extends ItemViewBinder<SubjectHeader, SubjectHeaderBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_subject_header, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull SubjectHeader item) {
        Context context = holder.headerImage.getContext();
        Glide.with(context).load(item.imageUri).into(holder.headerImage);

        holder.briefText.setText(item.brief);
        holder.briefText.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getString(R.string.DB1FontPath)));

        holder.text.setText(item.text);
        holder.text.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getString(R.string.LFontPath)));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView headerImage;
        TextView briefText;
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            headerImage = (ImageView) itemView.findViewById(R.id.header_image);
            briefText = (TextView) itemView.findViewById(R.id.brief_text);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }

}
