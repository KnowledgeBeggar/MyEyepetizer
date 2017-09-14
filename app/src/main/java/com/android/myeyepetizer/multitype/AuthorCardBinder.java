package com.android.myeyepetizer.multitype;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.myeyepetizer.AuthorDetailActivity;
import com.android.myeyepetizer.R;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Administrator on 2017/9/12.
 */

public class AuthorCardBinder extends ItemViewBinder<AuthorCard, AuthorCardBinder.ViewHolder> {

    private Activity mActivity;

    public AuthorCardBinder(Activity activity) {
        mActivity = activity;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_author_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final AuthorCard item) {
        Glide.with(mActivity).load(item.data.icon).into(holder.authorIcon);
        holder.authorName.setText(item.data.title);
        holder.authorDescription.setText(item.data.description);
        holder.clickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, AuthorDetailActivity.class);
                intent.putExtra(AuthorDetailActivity.FLAG_INTENT_ID, item.data.id);
                mActivity.startActivity(intent);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        Button button;
        CircleImageView authorIcon;
        TextView authorName;
        TextView authorDescription;
        LinearLayout clickLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            button = (Button) itemView.findViewById(R.id.collection_button);
            button.setTextColor(ContextCompat.getColor(mActivity, R.color.colorBlack));
            button.setBackgroundResource(R.drawable.ic_collection_button_black);

            authorIcon = (CircleImageView) itemView.findViewById(R.id.author_icon);

            authorName = (TextView) itemView.findViewById(R.id.author_name);
            authorName.setTextColor(ContextCompat.getColor(mActivity, R.color.colorBlack));
            authorName.setTypeface(Typeface.createFromAsset(mActivity.getAssets(), mActivity.getString(R.string.DB1FontPath)));

            authorDescription = (TextView) itemView.findViewById(R.id.author_description);
            authorDescription.setTextColor(ContextCompat.getColor(mActivity, R.color.colorBlack));
            authorDescription.setTypeface(Typeface.createFromAsset(mActivity.getAssets(), mActivity.getString(R.string.LFontPath)));

            clickLayout = (LinearLayout) itemView.findViewById(R.id.click_layout);
        }
    }

}
