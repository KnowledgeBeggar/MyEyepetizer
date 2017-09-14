package com.android.myeyepetizer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.myeyepetizer.Api.ReplyApi;
import com.android.myeyepetizer.MovieDetailActivity;
import com.android.myeyepetizer.R;
import com.android.myeyepetizer.RetrofitFactory;
import com.android.myeyepetizer.gson.Reply;
import com.android.myeyepetizer.gson.ReplyDataBean;
import com.android.myeyepetizer.multitype.Category;
import com.android.myeyepetizer.multitype.EndArea;
import com.android.myeyepetizer.multitype.EndAreaBinder;
import com.android.myeyepetizer.multitype.ReplyCategoryBinder;
import com.android.myeyepetizer.multitype.ReplyInfoItem;
import com.android.myeyepetizer.multitype.ReplyInfoItemBinder;
import com.android.myeyepetizer.utils.LoadMoreDelegate;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by Administrator on 2017/9/6.
 */

public class ReplyFragment extends Fragment {

    public static final ReplyFragment newInstance() {
        return new ReplyFragment();
    }

    private static final String REPLY_HOT_CATEGORY = "热  门  评  论";
    private static final String REPLY_RECENTLY_CATEGORY = "最  新  评  论";

    private ImageButton mCloseButton;
    private RecyclerView mRecyclerView;
    private MultiTypeAdapter mAdapter;
    private Items mItems;
    private MovieDetailActivity mActivity;
    private ReplyApi mReplyApi;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private String mNextPageUrl;
    private boolean mIsLoading = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reply_layout, container, false);
        mActivity = (MovieDetailActivity) getActivity();
        mCloseButton = (ImageButton) view.findViewById(R.id.close_button);
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                Fragment fragment = manager.findFragmentByTag("REPLY");
                manager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .remove(fragment)
                        .commit();
                mActivity.setRecyclerViewVisible();
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.reply_recycler_view);
        initRecyclerView();
        loadData();

        return view;
    }

    private void initRecyclerView() {
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(Category.class, new ReplyCategoryBinder());
        mAdapter.register(ReplyInfoItem.class, new ReplyInfoItemBinder());
        mAdapter.register(EndArea.class, new EndAreaBinder(EndAreaBinder.FLAG_TEXT_COLOR_WHITE));

        mItems = new Items();
        mAdapter.setItems(mItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadData() {
        mReplyApi = RetrofitFactory.getRetrofit().createApi(ReplyApi.class);
        Observable<ReplyDataBean> observable = mReplyApi.getReplyData(mActivity.getMovieId());
        mIsLoading = true;
        observable
                .filter(new Predicate<ReplyDataBean>() {
                    @Override
                    public boolean test(@NonNull ReplyDataBean replyDataBean) throws Exception {
                        return replyDataBean != null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReplyDataBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull ReplyDataBean replyDataBean) {
                        mNextPageUrl = replyDataBean.nextPageUrl;
                        boolean isHotOver = false;
                        boolean isHaveHotCategory = false;
                        boolean isHaveRecentlyCategory = false;

                        for (Reply reply : replyDataBean.replyList) {
                            if (!isHotOver && reply.hot) {
                                if (!isHaveHotCategory) {
                                    mItems.add(new Category(REPLY_HOT_CATEGORY));
                                    isHaveHotCategory = true;
                                }
                                mItems.add(new ReplyInfoItem(reply));
                                continue;
                            }

                            isHotOver = true;
                            if (!isHaveRecentlyCategory) {
                                mItems.add(new Category(REPLY_RECENTLY_CATEGORY));
                                isHaveRecentlyCategory = true;
                            }
                            mItems.add(new ReplyInfoItem(reply));
                        }
                        mAdapter.notifyDataSetChanged();
                        mIsLoading = false;
                        setRecyclerViewScrollListener();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(getActivity(), "评论加载失败", Toast.LENGTH_SHORT).show();
                        mIsLoading = false;
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setRecyclerViewScrollListener() {
        LoadMoreDelegate delegate = new LoadMoreDelegate(new LoadMoreDelegate.LoadMoreSubject() {
            @Override
            public boolean isLoading() {
                return mIsLoading;
            }

            @Override
            public void onLoadMore() {
                mIsLoading = true;
                if (mNextPageUrl != null) {
                    Observable<ReplyDataBean> observable = mReplyApi.loadMoreReply(mNextPageUrl);
                    observable
                            .filter(new Predicate<ReplyDataBean>() {
                                @Override
                                public boolean test(@NonNull ReplyDataBean replyDataBean) throws Exception {
                                    return replyDataBean != null;
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<ReplyDataBean>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                    mCompositeDisposable.add(d);
                                }

                                @Override
                                public void onNext(@NonNull ReplyDataBean replyDataBean) {
                                    for (Reply reply : replyDataBean.replyList) {
                                        mItems.add(new ReplyInfoItem(reply));
                                    }
                                    mNextPageUrl = replyDataBean.nextPageUrl;
                                    mAdapter.notifyDataSetChanged();
                                    mIsLoading = false;
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    Toast.makeText(getActivity(), "评论加载失败", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                } else {
                    mItems.add(new EndArea());
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        delegate.attach(mRecyclerView);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCompositeDisposable.dispose();
    }
}
