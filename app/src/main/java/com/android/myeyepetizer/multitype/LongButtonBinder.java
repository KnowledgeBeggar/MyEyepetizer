package com.android.myeyepetizer.multitype;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.myeyepetizer.DailyActivity;
import com.android.myeyepetizer.R;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Administrator on 2017/8/25.
 */

public class LongButtonBinder extends ItemViewBinder<LongButton, LongButtonBinder.ViewHolder> {

    private Activity mActivity;

    public LongButtonBinder(Activity activity) {
        mActivity = activity;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_long_button, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull LongButton item) {
        Context context = holder.text.getContext();
        holder.text.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.LFontPath)));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, DailyActivity.class);
                mActivity.startActivity(intent);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.long_button_text);
        }
    }

}
