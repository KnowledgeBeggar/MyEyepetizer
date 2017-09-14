package com.android.myeyepetizer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.myeyepetizer.Api.RankListApi;
import com.android.myeyepetizer.R;
import com.android.myeyepetizer.RetrofitFactory;
import com.android.myeyepetizer.gson.Data;
import com.android.myeyepetizer.gson.GetDataBean;
import com.android.myeyepetizer.gson.Item;
import com.android.myeyepetizer.multitype.VideoItem;
import com.android.myeyepetizer.multitype.VideoItemBinder;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by Administrator on 2017/9/7.
 */

public class RankViewPagerFragment extends RecyclerViewFragment{

    public static final String FLAG_WEEK = "周排行";
    public static final String FLAG_MONTH = "月排行";
    public static final String FLAG_HISTORICAL = "总排行";
    public static RankViewPagerFragment newInstance() {
        return new RankViewPagerFragment();
    }

    private String mLoadFlag;
    private RecyclerView mRecyclerView;
    private MultiTypeAdapter mAdapter;
    private Items mItems;
    private RankListApi mRankListApi;
    private Disposable mDisposable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initRecycler(view);
        loadData();

        return view;
    }

    private void loadData() {
        mRankListApi = RetrofitFactory.getRetrofit().createApi(RankListApi.class);
        Observable<GetDataBean> observable = null;
        switch (mLoadFlag) {
            case FLAG_WEEK:
                observable = mRankListApi.getWeekRankList();
                break;
            case FLAG_MONTH:
                observable = mRankListApi.getMonthRankList();
                break;
            case FLAG_HISTORICAL:
                observable = mRankListApi.getHistoricalList();
                break;
            default:
                break;
        }
        observable
                .filter(new Predicate<GetDataBean>() {
                    @Override
                    public boolean test(@NonNull GetDataBean getDataBean) throws Exception {
                        return getDataBean != null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetDataBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull GetDataBean getDataBean) {
                        for (Item item : getDataBean.itemList) {
                            mItems.add(new VideoItem(item.data));
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initRecycler(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(VideoItem.class, new VideoItemBinder(getActivity()));

        mItems = new Items();
        mAdapter.setItems(mItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setLoadFlag(String loadFlag) {
        mLoadFlag = loadFlag;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDisposable.dispose();
    }
}
