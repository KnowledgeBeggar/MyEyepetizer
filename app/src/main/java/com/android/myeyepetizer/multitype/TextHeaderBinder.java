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
import com.android.myeyepetizer.utils.AddSpaceToString;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Administrator on 2017/9/5.
 */

public class TextHeaderBinder extends ItemViewBinder<TextHeader, TextHeaderBinder.ViewHolder> {

    public static final int FLAG_HOME = 0;
    public static final int FLAG_FOUND = 1;

    private int mFlag;

    public TextHeaderBinder(int flag) {
        mFlag = flag;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_text_header, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull TextHeader item) {
        Context context = holder.textHeader.getContext();

        if (mFlag == 0) {
            holder.textHeader.setText(item.headerText);
            holder.textHeader.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getString(R.string.LobsterFontPath)));
        } else if (mFlag == 1) {
            holder.textHeader.setTextSize(16);
            holder.textHeader.setText(AddSpaceToString.addSpace(item.headerText));
            holder.textHeader.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getString(R.string.DB1FontPath)));
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textHeader;

        public ViewHolder(View itemView) {
            super(itemView);
            textHeader = (TextView) itemView;
        }
    }

}
