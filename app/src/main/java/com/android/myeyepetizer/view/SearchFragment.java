package com.android.myeyepetizer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.myeyepetizer.Api.SearchApi;
import com.android.myeyepetizer.R;
import com.android.myeyepetizer.RetrofitFactory;
import com.android.myeyepetizer.gson.GetDataBean;
import com.android.myeyepetizer.gson.Item;
import com.android.myeyepetizer.multitype.AuthorCollectionItem;
import com.android.myeyepetizer.multitype.AuthorCollectionItemBinder;
import com.android.myeyepetizer.multitype.EndArea;
import com.android.myeyepetizer.multitype.EndAreaBinder;
import com.android.myeyepetizer.multitype.VideoItem;
import com.android.myeyepetizer.multitype.VideoItemBinder;
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
 * Created by Administrator on 2017/9/13.
 */

public class SearchFragment extends RecyclerViewFragment {

    public static SearchFragment newInstance(GetDataBean getDataBean) {
        SearchFragment fragment = new SearchFragment();
        fragment.setGetDataBean(getDataBean);
        return fragment;
    }

    private RecyclerView mRecyclerView;
    private MultiTypeAdapter mAdapter;
    private Items mItems;
    private GetDataBean mGetDataBean;
    private SearchApi mSearchApi;
    private String mNextPageUrl;
    private boolean mIsLoading;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private Observer<GetDataBean> mObserver = new Observer<GetDataBean>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            mCompositeDisposable.add(d);
        }

        @Override
        public void onNext(@NonNull GetDataBean getDataBean) {
            solveData(getDataBean);
            mAdapter.notifyDataSetChanged();
            mNextPageUrl = getDataBean.nextPageUrl;
            mIsLoading = false;
        }

        @Override
        public void onError(@NonNull Throwable e) {
            mIsLoading = false;
            Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  super.onCreateView(inflater, container, savedInstanceState);
        initRecycler(view);
        loadData();
        return view;
    }

    private void loadData() {
        mNextPageUrl = mGetDataBean.nextPageUrl;
        mSearchApi = RetrofitFactory.getRetrofit().createApi(SearchApi.class);
        solveData(mGetDataBean);
        setRecyclerViewScrollListener();
    }

    private void solveData(GetDataBean getDataBean) {
        for (Item item : getDataBean.itemList) {
            switch (item.type) {
                case "videoCollectionWithBrief":
                    if (item.data.header.ifPgc) {
                        mItems.add(new AuthorCollectionItem(item.data, AuthorCollectionItemBinder.FLAG_INTENT_AUTHOR));
                    } else {
                        mItems.add(new AuthorCollectionItem(item.data, AuthorCollectionItemBinder.FLAG_INTENT_ALBUM));
                    }
                    break;
                case "video":
                    mItems.add(new VideoItem(item.data));
                    break;
                default:
                    break;
            }
        }
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
                    Observable<GetDataBean> observable = mSearchApi.loadMoreData(mNextPageUrl);
                    observable
                            .filter(new Predicate<GetDataBean>() {
                                @Override
                                public boolean test(@NonNull GetDataBean getDataBean) throws Exception {
                                    return getDataBean != null;
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(mObserver);
                } else {
                    mItems.add(new EndArea());
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        delegate.attach(mRecyclerView);
    }

    private void initRecycler(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(AuthorCollectionItem.class, new AuthorCollectionItemBinder(getActivity()));
        mAdapter.register(VideoItem.class, new VideoItemBinder(getActivity()));
        mAdapter.register(EndArea.class, new EndAreaBinder(EndAreaBinder.FLAG_TEXT_COLOR_BLACK));

        mItems = new Items();
        mAdapter.setItems(mItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setGetDataBean(GetDataBean getDataBean) {
        mGetDataBean = getDataBean;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCompositeDisposable.dispose();
    }
}
