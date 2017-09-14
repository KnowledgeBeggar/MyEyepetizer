package com.android.myeyepetizer.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/9/5.
 */

public class LoadMoreDelegate {

    private final LoadMoreSubject mLoadMoreSubject;

    public LoadMoreDelegate(LoadMoreSubject loadMoreSubject) {
        mLoadMoreSubject = loadMoreSubject;
    }

    public void attach(RecyclerView recyclerView) {
        final LinearLayoutManager linear = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new EndlessScrollListener(linear, mLoadMoreSubject));
    }

    private static class EndlessScrollListener extends RecyclerView.OnScrollListener {

        private static final int VISIBLE_THRESHOLD = 6;
        private final LinearLayoutManager mLayoutManager;
        private final LoadMoreSubject mLoadMoreSubject;

        public EndlessScrollListener(LinearLayoutManager layoutManager, LoadMoreSubject loadMoreSubject) {
            mLayoutManager = layoutManager;
            mLoadMoreSubject = loadMoreSubject;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (dy < 0 || mLoadMoreSubject.isLoading()) {
                return;
            }
            final int itemCount = mLayoutManager.getItemCount();
            final int lastVisiblePosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
            final boolean isBottom = (lastVisiblePosition >= itemCount - VISIBLE_THRESHOLD);
            if (isBottom) {
                mLoadMoreSubject.onLoadMore();
            }
        }
    }

    public interface LoadMoreSubject {
        boolean isLoading();
        void onLoadMore();
    }

}
