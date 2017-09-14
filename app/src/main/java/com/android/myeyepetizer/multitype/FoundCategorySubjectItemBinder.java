package com.android.myeyepetizer.multitype;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.LoopViewPager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.myeyepetizer.R;
import com.android.myeyepetizer.RecyclerViewActivity;
import com.android.myeyepetizer.SubjectActivity;
import com.android.myeyepetizer.adpter.FragmentAdapter;
import com.android.myeyepetizer.adpter.ImageButtonAdpter;
import com.android.myeyepetizer.gson.Item;
import com.android.myeyepetizer.utils.AddSpaceToString;
import com.android.myeyepetizer.utils.TranslateDptoPiexel;
import com.android.myeyepetizer.view.FoundViewPagerFragment;
import com.android.myeyepetizer.view.HomeTopPagerFragment;
import com.android.myeyepetizer.view.ImageButtonFragment;
import com.bumptech.glide.Glide;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Administrator on 2017/9/9.
 */

public class FoundCategorySubjectItemBinder extends ItemViewBinder<FoundCategorySubjectItem, FoundCategorySubjectItemBinder.ViewHolder> {

    private FragmentManager mManager;
    private Activity mActivity;

    public FoundCategorySubjectItemBinder(FragmentManager manager, Activity activity) {
        mManager = manager;
        mActivity = activity;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_found_category_subject, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull FoundCategorySubjectItem item) {
        holder.subjectButton.setText(AddSpaceToString.addSpace(item.data.header.title));
        holder.subjectButton.setTypeface(Typeface.createFromAsset(mActivity.getAssets(), mActivity.getString(R.string.DB1FontPath)));
        holder.subjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, RecyclerViewActivity.class);
                intent.putExtra(RecyclerViewActivity.INTENT_FLAG, RecyclerViewActivity.FLAG_HOT_SUBJECT);
                mActivity.startActivity(intent);
            }
        });

        List<ImageButton> imageButtons = new ArrayList<>();
        for (final Item item1 : item.data.itemList) {
            ImageButton imageButton = new ImageButton(mActivity);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageButton.setLayoutParams(params);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, SubjectActivity.class);
                    intent.putExtra("ID", item1.data.id);
                    mActivity.startActivity(intent);
                }
            });
            Glide.with(mActivity).load(item1.data.image).into(imageButton);

            imageButtons.add(imageButton);
        }

        holder.setImageButtons(imageButtons);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        Button subjectButton;
        ViewPager viewPager;
        ImageButtonAdpter adapter;

        public ViewHolder(View itemView) {
            super(itemView);
            subjectButton = (Button) itemView.findViewById(R.id.subject_button);
            viewPager = (ViewPager) itemView.findViewById(R.id.subject_view_pager);
            viewPager.setOffscreenPageLimit(3);
            adapter = new ImageButtonAdpter(null);
            viewPager.setAdapter(adapter);
        }

        public void setImageButtons(List<ImageButton> imageButtons) {
            adapter.setViewList(imageButtons);
            adapter.notifyDataSetChanged();
        }
    }

}
