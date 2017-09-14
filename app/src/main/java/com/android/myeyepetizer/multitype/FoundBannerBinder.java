package com.android.myeyepetizer.multitype;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.myeyepetizer.R;
import com.android.myeyepetizer.gson.Item;
import com.android.myeyepetizer.utils.GlideImageLoader;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Administrator on 2017/9/7.
 */

public class FoundBannerBinder extends ItemViewBinder<Banner, FoundBannerBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_found_banner, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Banner item) {
        List<String> images = new ArrayList<>();
        for (Item item1 : item.data.itemList) {
            images.add(item1.data.image);
        }
        holder.banner.setImages(images).start();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        com.youth.banner.Banner banner;

        public ViewHolder(View itemView) {
            super(itemView);
            banner = (com.youth.banner.Banner) itemView;
            banner.setImageLoader(new GlideImageLoader());
        }
    }

}
