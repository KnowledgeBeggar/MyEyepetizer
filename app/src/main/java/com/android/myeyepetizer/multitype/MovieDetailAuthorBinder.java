package com.android.myeyepetizer.multitype;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.myeyepetizer.AuthorDetailActivity;
import com.android.myeyepetizer.R;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.multitype.ItemViewBinder;

public class MovieDetailAuthorBinder extends ItemViewBinder<MovieDetailAuthor, MovieDetailAuthorBinder.ViewHolder> {

    private Activity mActivity;

    public MovieDetailAuthorBinder(Activity activity) {
        mActivity = activity;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_movie_author, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final MovieDetailAuthor item) {
        Context context = holder.authorIcon.getContext();
        Glide.with(context).load(item.icon).into(holder.authorIcon);
        holder.name.setText(item.name);

        holder.description.setText(item.description);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, AuthorDetailActivity.class);
                intent.putExtra(AuthorDetailActivity.FLAG_INTENT_ID, item.id);
                mActivity.startActivity(intent);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView authorIcon;
        TextView name;
        TextView description;
        Button button;

        public ViewHolder(View itemView) {
            super(itemView);
            authorIcon = (CircleImageView) itemView.findViewById(R.id.author_icon);
            name = (TextView) itemView.findViewById(R.id.author_name);
            name.setTypeface(Typeface.createFromAsset(mActivity.getAssets(), mActivity.getResources().getString(R.string.DB1FontPath)));
            description = (TextView) itemView.findViewById(R.id.author_description);
            description.setTypeface(Typeface.createFromAsset(mActivity.getAssets(), mActivity.getResources().getString(R.string.LFontPath)));
            button = (Button) itemView.findViewById(R.id.collection_button);
        }
    }

}
