package com.android.myeyepetizer.multitype;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.myeyepetizer.R;
import com.android.myeyepetizer.utils.AddSpaceToString;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Administrator on 2017/9/12.
 */

public class LeftAlignTextHeaderBinder extends ItemViewBinder<LeftAlignTextHeader, LeftAlignTextHeaderBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_left_align_text_header, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull LeftAlignTextHeader item) {
        holder.text.setText(AddSpaceToString.addSpace(item.headerText));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
            text.setTypeface(Typeface.createFromAsset(text.getContext().getAssets(), text.getContext().getString(R.string.LFontPath)));
        }
    }

}
