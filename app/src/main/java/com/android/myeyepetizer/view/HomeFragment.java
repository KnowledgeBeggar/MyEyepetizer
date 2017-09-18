package com.android.myeyepetizer.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.myeyepetizer.Api.HomeApi;
import com.android.myeyepetizer.R;
import com.android.myeyepetizer.RetrofitFactory;
import com.android.myeyepetizer.gson.Data;
import com.android.myeyepetizer.gson.GetDataBean;
import com.android.myeyepetizer.gson.Item;
import com.android.myeyepetizer.multitype.GreyArea;
import com.android.myeyepetizer.multitype.GreyAreaBinder;
import com.android.myeyepetizer.multitype.Banner;
import com.android.myeyepetizer.multitype.BannerBinder;
import com.android.myeyepetizer.multitype.HomeItem;
import com.android.myeyepetizer.multitype.HomeItemBinder;
import com.android.myeyepetizer.multitype.HomeItemCollection;
import com.android.myeyepetizer.multitype.HomeItemCollectionBinder;
import com.android.myeyepetizer.multitype.TextHeader;
import com.android.myeyepetizer.multitype.TextHeaderBinder;
import com.android.myeyepetizer.multitype.HomeTopPager;
import com.android.myeyepetizer.multitype.HomeTopPagerBinder;
import com.android.myeyepetizer.multitype.Line;
import com.android.myeyepetizer.multitype.GreyLineBinder;
import com.android.myeyepetizer.multitype.LongButton;
import com.android.myeyepetizer.multitype.LongButtonBinder;
import com.android.myeyepetizer.utils.DataPreference;
import com.android.myeyepetizer.utils.LoadMoreDelegate;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

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
 * Created by Administrator on 2017/8/17.
 */

public class HomeFragment extends Fragment {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private static final String SPLIT_REGEX = "&page=";
    private static final String KEY_SHARED_PREFERENCE = "HOME_DATA";

    private MultiTypeAdapter mAdapter;
    private Items mItems;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private RecyclerView mRecyclerView;
    private HomeApi mHomeApi;
    private SwipeRefreshLayout mRefreshLayout;

    private boolean mIsLoading;
    private String mNextPageUrl;
    private long mLastDate;

    private Observer<GetDataBean> mObserver = new Observer<GetDataBean>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            mCompositeDisposable.add(d);
        }

        @Override
        public void onNext(@NonNull GetDataBean getDataBean) {
            if (mLastDate == 0L || mLastDate != getDataBean.date ) {
                DataPreference.setLastPrefHomeData(getActivity(), new Gson().toJson(getDataBean));
                DataPreference.setLastPrefHomeDate(getActivity(), getDataBean.date);
            }
            solveData(getDataBean);
            mRefreshLayout.setRefreshing(false);
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
        final View view = inflater.inflate(R.layout.fragment_home_layout, container, false);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorBlue));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        mLastDate = DataPreference.getLastPrefHomeDate(getActivity());
        initRecyclerView(view);
        loadData();
        setRecyclerViewScrollListener();
        return view;

    }

    private void initRecyclerView(final View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(HomeTopPager.class, new HomeTopPagerBinder());
        mAdapter.register(LongButton.class, new LongButtonBinder(getActivity()));
        mAdapter.register(Line.class, new GreyLineBinder());
        mAdapter.register(GreyArea.class, new GreyAreaBinder());
        mAdapter.register(Banner.class, new BannerBinder(getActivity()));
        mAdapter.register(TextHeader.class, new TextHeaderBinder(TextHeaderBinder.FLAG_HOME));
        mAdapter.register(HomeItem.class, new HomeItemBinder(getActivity()));
        mAdapter.register(HomeItemCollection.class, new HomeItemCollectionBinder(getActivity()));

        mItems = new Items();
        mAdapter.setItems(mItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initViewpager(List<Data> pagerDatas) {
        mItems.add(new HomeTopPager(pagerDatas, getChildFragmentManager(), getActivity()));
        mItems.add(new LongButton());
        mItems.add(new Line());
        mItems.add(new GreyArea());
    }

    private void loadData() {
        String data;
        if ((data = DataPreference.getLastPrefHomeData(getActivity())) != null) {
            GetDataBean getDataBean = new Gson().fromJson(data, GetDataBean.class);
            solveData(getDataBean);
            return;
        }
        loadDataByInternet();
    }

    private void loadDataByInternet() {
        mHomeApi = RetrofitFactory.getRetrofit().createApi(HomeApi.class);
        Observable<GetDataBean> observable = mHomeApi.getHomeItem();
        mIsLoading = true;
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

    private void solveData(GetDataBean getDataBean) {
        mItems.clear();
        List<Data> pagerDatas = new ArrayList<>();
        List<Data> homeItemDatas = new ArrayList<>();
        Data bannerData = null;
        Data textHeader = null;
        Data videoCollection = null;
        for (Item item : getDataBean.itemList) {
            if (item.type.equals("banner2")) {
                bannerData = item.data;
            } else if (item.type.equals("video") && item.tag.equals("0") && item.data.cover.homePageCover != null) {
                pagerDatas.add(item.data);
            } else if (item.type.equals("video") && !item.tag.equals("0")) {
                homeItemDatas.add(item.data);
            } else if (item.type.equals("textHeader")) {
                textHeader = item.data;
            } else if (item.type.equals("videoCollectionWithCover")) {
                videoCollection = item.data;
            }
        }
        initViewpager(pagerDatas);
        if (bannerData != null) {
            mItems.add(new Banner(bannerData));
        }
        if (videoCollection != null) {
            mItems.add(new HomeItemCollection(videoCollection));
        }
        if (textHeader != null) {
            mItems.add(new TextHeader(textHeader.text));
        }
        for (Data data : homeItemDatas) {
            mItems.add(new HomeItem(data));
        }

        mAdapter.notifyDataSetChanged();
        mNextPageUrl = getDataBean.nextPageUrl.split(SPLIT_REGEX)[0];
        mIsLoading = false;
    }

    private void setRecyclerViewScrollListener() {
        final Observer<GetDataBean> observer = new Observer<GetDataBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(@NonNull GetDataBean getDataBean) {
                for (Item item : getDataBean.itemList) {
                    if (item.type.equals("textHeader")) {
                        mItems.add(new TextHeader(item.data.text));
                    } else if (item.type.equals("video")) {
                        mItems.add(new HomeItem(item.data));
                    }
                }
                mAdapter.notifyDataSetChanged();
                mIsLoading = false;
                mNextPageUrl = getDataBean.nextPageUrl.split(SPLIT_REGEX)[0];
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
        LoadMoreDelegate delegate = new LoadMoreDelegate(new LoadMoreDelegate.LoadMoreSubject() {
            @Override
            public boolean isLoading() {
                return mIsLoading;
            }

            @Override
            public void onLoadMore() {
                mIsLoading = true;
                if (mHomeApi == null) {
                    mHomeApi = RetrofitFactory.getRetrofit().createApi(HomeApi.class);
                }
                Observable<GetDataBean> observable = mHomeApi.loadMoreItem(mNextPageUrl);
                observable
                        .filter(new Predicate<GetDataBean>() {
                            @Override
                            public boolean test(@NonNull GetDataBean getDataBean) throws Exception {
                                return getDataBean != null;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(observer);
            }
        });
        delegate.attach(mRecyclerView);
    }

    private void refreshData() {
        loadDataByInternet();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
    }
}
