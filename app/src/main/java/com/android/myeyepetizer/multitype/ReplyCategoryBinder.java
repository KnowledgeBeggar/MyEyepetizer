package com.android.myeyepetizer.multitype;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.myeyepetizer.R;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Administrator on 2017/9/7.
 */

public class ReplyCategoryBinder extends ItemViewBinder<Category, ReplyCategoryBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_reply_category, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Category item) {
        holder.categoryText.setText(item.categoryText);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView categoryText;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryText = (TextView) itemView.findViewById(R.id.category);
        }
    }

}
