package com.android.myeyepetizer.multitype;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.myeyepetizer.CategoryDetailActivity;
import com.android.myeyepetizer.R;
import com.android.myeyepetizer.RankActivity;
import com.android.myeyepetizer.RecyclerViewActivity;
import com.android.myeyepetizer.utils.TranslateDptoPiexel;
import com.bumptech.glide.Glide;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Administrator on 2017/9/7.
 */

public class SquareCardBinder extends ItemViewBinder<Banner, SquareCardBinder.ViewHolder> {

    public static final int FLAG_START_RANK_ACTIVITY = 0;
    public static final int FLAG_START_CATEGORY_ACTIVITY = 1;
    private Activity mActivity;
    private int mFlag;

    public SquareCardBinder(Activity activity) {
        mActivity = activity;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_square_card_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Banner item) {
        mFlag = item.intentFlag;
        Glide.with(mActivity).load(item.data.image).into(holder.imageButton);
        View.OnClickListener listener = null;
        switch (mFlag) {
            case FLAG_START_RANK_ACTIVITY:
                holder.cover.setVisibility(View.GONE);
                listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, RankActivity.class);
                        intent.putExtra("Category", item.data.id);
                        mActivity.startActivity(intent);
                    }
                };
                break;
            case FLAG_START_CATEGORY_ACTIVITY:
                if (item.data.dataType.equals("ActionCard")) {
                    holder.categoryName.setText(item.data.text);
                    listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mActivity, RecyclerViewActivity.class);
                            intent.putExtra(RecyclerViewActivity.INTENT_FLAG, RecyclerViewActivity.FLAG_ALL_CATEGORY);
                            mActivity.startActivity(intent);
                        }
                    };
                } else {
                    holder.categoryName.setText(item.data.title);
                    listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = null;
                            switch (item.data.id) {
                                case -1:
                                    intent = new Intent(mActivity, RankActivity.class);
                                    break;
                                case 0:
                                    intent = new Intent(mActivity, RecyclerViewActivity.class);
                                    intent.putExtra(RecyclerViewActivity.INTENT_FLAG, RecyclerViewActivity.FLAG_HOT_SUBJECT);
                                    break;
                                default:
                                    intent = new Intent(mActivity, CategoryDetailActivity.class);
                                    intent.putExtra(CategoryDetailActivity.INTENT_ID_KEY, item.data.id);
                                    break;
                            }
                            mActivity.startActivity(intent);
                        }
                    };
                }
                break;
            default:
                break;
        }
        holder.imageButton.setOnClickListener(listener);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        FrameLayout cover;
        ImageButton imageButton;
        TextView categoryName;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            cover = (FrameLayout) itemView.findViewById(R.id.cover_layout);
            imageButton = (ImageButton) itemView.findViewById(R.id.rank_image);
            categoryName = (TextView) itemView.findViewById(R.id.category_name);

            if (mActivity instanceof RecyclerViewActivity) {
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        TranslateDptoPiexel.translate(210, mActivity));
                cardView.setLayoutParams(params);
            }
        }
    }

}
