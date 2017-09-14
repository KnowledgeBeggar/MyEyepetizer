package com.android.myeyepetizer.multitype;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.myeyepetizer.R;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Administrator on 2017/9/4.
 */

public class EndAreaBinder extends ItemViewBinder<EndArea, EndAreaBinder.ViewHolder> {

    public static final int FLAG_TEXT_COLOR_WHITE = 0;
    public static final int FLAG_TEXT_COLOR_BLACK = 1;

    private int mFlag;

    public EndAreaBinder(int flag) {
        mFlag = flag;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_movie_end_text, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull EndArea item) {
        Context context = holder.text.getContext();
        holder.text.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.LobsterFontPath)));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        FrameLayout background;
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.end_text);
            background = (FrameLayout) itemView.findViewById(R.id.end_layout);
            if (mFlag == FLAG_TEXT_COLOR_BLACK) {
                text.setTextColor(ContextCompat.getColor(text.getContext(), R.color.colorBlack));
                background.setBackgroundColor(ContextCompat.getColor(background.getContext(), R.color.colorWhite));
            }
        }
    }

}
