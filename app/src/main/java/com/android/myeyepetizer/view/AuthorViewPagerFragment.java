package com.android.myeyepetizer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.myeyepetizer.Api.AuthorApi;
import com.android.myeyepetizer.R;
import com.android.myeyepetizer.RetrofitFactory;
import com.android.myeyepetizer.gson.Author;
import com.android.myeyepetizer.gson.GetDataBean;
import com.android.myeyepetizer.gson.Item;
import com.android.myeyepetizer.gson.Tab;
import com.android.myeyepetizer.multitype.AuthorCollectionItem;
import com.android.myeyepetizer.multitype.AuthorCollectionItemBinder;
import com.android.myeyepetizer.multitype.EndArea;
import com.android.myeyepetizer.multitype.EndAreaBinder;
import com.android.myeyepetizer.multitype.FoundCategoryDetailCollection;
import com.android.myeyepetizer.multitype.FoundCategoryDetailCollectionBinder;
import com.android.myeyepetizer.multitype.GreyArea;
import com.android.myeyepetizer.multitype.GreyAreaBinder;
import com.android.myeyepetizer.multitype.GreyLineBinder;
import com.android.myeyepetizer.multitype.Line;
import com.android.myeyepetizer.multitype.MovieDetailRelateItem;
import com.android.myeyepetizer.multitype.MovieDetailRelateItemBinder;
import com.android.myeyepetizer.multitype.TextHeader;
import com.android.myeyepetizer.multitype.TextHeaderBinder;
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

public class AuthorViewPagerFragment extends RecyclerViewFragment {

    public static AuthorViewPagerFragment newInstance(Tab tab) {
        AuthorViewPagerFragment fragment = new AuthorViewPagerFragment();
        fragment.setTab(tab);
        return fragment;
    }

    private Tab mTab;
    private RecyclerView mRecyclerView;
    private MultiTypeAdapter mAdapter;
    private Items mItems;
    private AuthorApi mAuthorApi;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private String mNextPageUrl;
    private boolean mIsLoading;

    private Observer<GetDataBean> mObserver = new Observer<GetDataBean>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            mCompositeDisposable.add(d);
        }

        @Override
        public void onNext(@NonNull GetDataBean getDataBean) {
            solveData(getDataBean);
            mNextPageUrl = getDataBean.nextPageUrl;
            mIsLoading = false;
        }

        @Override
        public void onError(@NonNull Throwable e) {
            Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
            mIsLoading = false;
        }

        @Override
        public void onComplete() {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initRecyclerView(view);
        loadData();
        return view;
    }

    private void loadData() {
        mAuthorApi = RetrofitFactory.getRetrofit().createApi(AuthorApi.class);
        Observable<GetDataBean> observable = mAuthorApi.loadMoreData(mTab.apiUrl);
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
        setRecyclerViewSrollListener();
    }

    private void solveData(GetDataBean getDataBean) {
        switch (mTab.name) {
            case "首页":
                addHomePageItem(getDataBean);
                break;
            case "全部":
                addAllPageItem(getDataBean);
                break;
            case "专辑":
                addPlayListItem(getDataBean);
                break;
            default:
                break;
        }
        mAdapter.notifyDataSetChanged();
    }

    private void setRecyclerViewSrollListener() {
        LoadMoreDelegate delegate = new LoadMoreDelegate(new LoadMoreDelegate.LoadMoreSubject() {
            @Override
            public boolean isLoading() {
                return mIsLoading;
            }

            @Override
            public void onLoadMore() {
                mIsLoading = true;
                if (mNextPageUrl != null) {
                    Observable<GetDataBean> observable = mAuthorApi.loadMoreData(mNextPageUrl);
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

    private void addHomePageItem(GetDataBean getDataBean) {
        for (Item item : getDataBean.itemList) {
            switch (item.type) {
                case "videoCollectionOfHorizontalScrollCard":
                    mItems.add(new FoundCategoryDetailCollection(item.data));
                    break;
                case "textHeader":
                    mItems.add(new Line());
                    mItems.add(new GreyArea());
                    mItems.add(new TextHeader(item.data.text));
                    break;
                case "video":
                    mItems.add(new MovieDetailRelateItem(item.data));
                    break;
                case "videoCollectionWithBrief":
                    if (item.data.header.ifPgc) {
                        mItems.add(new AuthorCollectionItem(item.data, AuthorCollectionItemBinder.FLAG_INTENT_AUTHOR));
                    } else {
                        mItems.add(new AuthorCollectionItem(item.data, AuthorCollectionItemBinder.FLAG_INTENT_ALBUM));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void addAllPageItem(GetDataBean getDataBean) {
        for (Item item : getDataBean.itemList) {
            if (item.type.equals("video")) {
                mItems.add(new MovieDetailRelateItem(item.data));
            }
        }
    }

    private void addPlayListItem(GetDataBean getDataBean) {
        for (Item item : getDataBean.itemList) {
            if (item.type.equals("videoCollectionWithBrief")) {
                mItems.add(new AuthorCollectionItem(item.data, AuthorCollectionItemBinder.FLAG_INTENT_ALBUM));
            }
        }
    }

    private void initRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(Line.class, new GreyLineBinder());
        mAdapter.register(GreyArea.class, new GreyAreaBinder());
        mAdapter.register(TextHeader.class, new TextHeaderBinder(TextHeaderBinder.FLAG_FOUND));
        mAdapter.register(EndArea.class, new EndAreaBinder(EndAreaBinder.FLAG_TEXT_COLOR_BLACK));
        mAdapter.register(FoundCategoryDetailCollection.class, new FoundCategoryDetailCollectionBinder(getChildFragmentManager(), getActivity()));
        mAdapter.register(AuthorCollectionItem.class, new AuthorCollectionItemBinder(getActivity()));
        mAdapter.register(MovieDetailRelateItem.class, new MovieDetailRelateItemBinder(getActivity(), MovieDetailRelateItemBinder.FLAG_BLACK_WORD_COLOR));

        mItems = new Items();
        mAdapter.setItems(mItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setTab(Tab tab) {
        mTab = tab;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCompositeDisposable.dispose();
    }
}
