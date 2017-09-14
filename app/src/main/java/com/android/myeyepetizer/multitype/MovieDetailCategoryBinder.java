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
 * Created by Administrator on 2017/9/4.
 */

public class MovieDetailCategoryBinder extends ItemViewBinder<Category, MovieDetailCategoryBinder.ViewHolder> {


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_movie_category_button, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Category item) {
        Context context = holder.categoryText.getContext();
        holder.categoryText.setText(AddSpaceToString.addSpace(item.categoryText));
        holder.categoryText.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.LFontPath)));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView categoryText;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryText = (TextView) itemView.findViewById(R.id.category);
        }
    }

}
