package com.android.myeyepetizer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.myeyepetizer.Api.FoundApi;
import com.android.myeyepetizer.R;
import com.android.myeyepetizer.RetrofitFactory;
import com.android.myeyepetizer.gson.Data;
import com.android.myeyepetizer.gson.GetDataBean;
import com.android.myeyepetizer.gson.Item;
import com.android.myeyepetizer.gson.Tab;
import com.android.myeyepetizer.multitype.AuthorCard;
import com.android.myeyepetizer.multitype.AuthorCardBinder;
import com.android.myeyepetizer.multitype.AuthorCollectionItem;
import com.android.myeyepetizer.multitype.AuthorCollectionItemBinder;
import com.android.myeyepetizer.multitype.Banner;
import com.android.myeyepetizer.multitype.CollectionItem;
import com.android.myeyepetizer.multitype.EndArea;
import com.android.myeyepetizer.multitype.EndAreaBinder;
import com.android.myeyepetizer.multitype.FoundBannerBinder;
import com.android.myeyepetizer.multitype.FoundCategoryDetailCollection;
import com.android.myeyepetizer.multitype.FoundCategoryDetailCollectionBinder;
import com.android.myeyepetizer.multitype.FoundCategoryItem;
import com.android.myeyepetizer.multitype.FoundCategoryItemBinder;
import com.android.myeyepetizer.multitype.FoundCategorySubjectItem;
import com.android.myeyepetizer.multitype.FoundCategorySubjectItemBinder;
import com.android.myeyepetizer.multitype.GreyArea;
import com.android.myeyepetizer.multitype.GreyAreaBinder;
import com.android.myeyepetizer.multitype.GreyLineBinder;
import com.android.myeyepetizer.multitype.HomeItem;
import com.android.myeyepetizer.multitype.HomeItemBinder;
import com.android.myeyepetizer.multitype.LeftAlignTextHeader;
import com.android.myeyepetizer.multitype.LeftAlignTextHeaderBinder;
import com.android.myeyepetizer.multitype.Line;
import com.android.myeyepetizer.multitype.SquareCardBinder;
import com.android.myeyepetizer.multitype.SquareCardCollectionBinder;
import com.android.myeyepetizer.multitype.TextHeader;
import com.android.myeyepetizer.multitype.TextHeaderBinder;
import com.android.myeyepetizer.multitype.VerticalLineBinder;
import com.android.myeyepetizer.utils.DataPreference;
import com.android.myeyepetizer.utils.LoadMoreDelegate;
import com.google.gson.Gson;

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

public class FoundViewPagerFragment extends RecyclerViewFragment {

    public static FoundViewPagerFragment newInstance(Tab tab) {
        FoundViewPagerFragment fragment = new FoundViewPagerFragment();
        fragment.setTab(tab);
        return fragment;
    }

    private RecyclerView mRecyclerView;
    private Tab mTab;
    private MultiTypeAdapter mAdapter;
    private Items mItems;
    private FoundApi mFoundApi;
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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        initRecyclerView();
        loadData();

        return view;
    }

    private void loadData() {
        mFoundApi = RetrofitFactory.getRetrofit().createApi(FoundApi.class);
        Observable<GetDataBean> observable = mFoundApi.getFoundCategoryData(mTab.apiUrl);
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
        setRecyclerViewScrollListener();
    }

    private void solveData(GetDataBean getDataBean) {
        switch (mTab.name) {
            case "热门":
                addHotItem(getDataBean);
                break;
            case "分类":
                addCategoryItem(getDataBean);
                break;
            case "作者":
                addAuthorItem(getDataBean);
                break;
            default:
                break;
        }
        mNextPageUrl = getDataBean.nextPageUrl;
        if (mNextPageUrl == null) {
            mItems.add(new EndArea());
        }
        mAdapter.notifyDataSetChanged();
    }

    private void addHotItem(GetDataBean getDataBean) {
        for (Item item : getDataBean.itemList) {
            switch (item.type) {
                case "horizontalScrollCard":
                    mItems.add(new Banner(item.data));
                    mItems.add(new GreyArea());
                    break;
                case "textHeader":
                    mItems.add(new TextHeader(item.data.text));
                    break;
                case "video":
                    mItems.add(new HomeItem(item.data));
                    break;
                case "squareCardCollection":
                    mItems.add(new GreyArea());
                    mItems.add(new CollectionItem(item.data, SquareCardBinder.FLAG_START_RANK_ACTIVITY));
                    mItems.add(new GreyArea());
                    break;
                default:
                    break;
            }
        }
    }

    private void addCategoryItem(GetDataBean getDataBean) {
        for (Item item : getDataBean.itemList) {
            switch (item.type) {
                case "squareCardCollection":
                    mItems.add(new CollectionItem(item.data, SquareCardBinder.FLAG_START_CATEGORY_ACTIVITY));
                    mItems.add(new Line());
                    mItems.add(new GreyArea());
                    break;
                case "bannerCollection":
                    mItems.add(new FoundCategorySubjectItem(item.data));
                    mItems.add(new Line());
                    mItems.add(new GreyArea());
                    break;
                case "videoCollectionOfHorizontalScrollCard":
                    mItems.add(new FoundCategoryItem(item.data));
                    mItems.add(new Line());
                    mItems.add(new GreyArea());
                    break;
                default:
                    break;
            }
        }
    }

    private void addAuthorItem(GetDataBean getDataBean) {
        for (Item item : getDataBean.itemList) {
            switch (item.type) {
                case "leftAlignTextHeader":
                    mItems.add(new LeftAlignTextHeader(item.data.text));
                    break;
                case "briefCard":
                    mItems.add(new AuthorCard(item.data));
                    break;
                case "videoCollectionWithBrief":
                    mItems.add(new AuthorCollectionItem(item.data, AuthorCollectionItemBinder.FLAG_INTENT_AUTHOR));
                    break;
                default:
                    break;
            }
        }
    }

    private void initRecyclerView() {
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(Banner.class, new FoundBannerBinder());
        mAdapter.register(TextHeader.class, new TextHeaderBinder(TextHeaderBinder.FLAG_FOUND));
        mAdapter.register(HomeItem.class, new HomeItemBinder(getActivity()));
        mAdapter.register(GreyArea.class, new GreyAreaBinder());
        mAdapter.register(CollectionItem.class, new SquareCardCollectionBinder(getActivity()));
        mAdapter.register(Line.class, new GreyLineBinder());
        mAdapter.register(FoundCategorySubjectItem.class, new FoundCategorySubjectItemBinder(getChildFragmentManager(), getActivity()));
        mAdapter.register(FoundCategoryItem.class, new FoundCategoryItemBinder(getActivity()));
        mAdapter.register(EndArea.class, new EndAreaBinder(EndAreaBinder.FLAG_TEXT_COLOR_BLACK));
        mAdapter.register(LeftAlignTextHeader.class, new LeftAlignTextHeaderBinder());
        mAdapter.register(AuthorCard.class, new AuthorCardBinder(getActivity()));
        mAdapter.register(AuthorCollectionItem.class, new AuthorCollectionItemBinder(getActivity()));

        mItems = new Items();
        mAdapter.setItems(mItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
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
                    Observable<GetDataBean> observable = mFoundApi.getFoundCategoryData(mNextPageUrl);
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
                }
            }
        });
        delegate.attach(mRecyclerView);
    }

    public void setTab(Tab tab) {
        mTab = tab;
    }
}
