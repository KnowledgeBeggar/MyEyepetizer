package com.android.myeyepetizer.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.myeyepetizer.Api.FollowApi;
import com.android.myeyepetizer.R;
import com.android.myeyepetizer.RecyclerViewActivity;
import com.android.myeyepetizer.RetrofitFactory;
import com.android.myeyepetizer.SearchActivity;
import com.android.myeyepetizer.gson.GetDataBean;
import com.android.myeyepetizer.gson.Item;
import com.android.myeyepetizer.multitype.AuthorCollectionItem;
import com.android.myeyepetizer.multitype.AuthorCollectionItemBinder;
import com.android.myeyepetizer.multitype.EndArea;
import com.android.myeyepetizer.multitype.EndAreaBinder;
import com.android.myeyepetizer.multitype.FoundCategoryItem;
import com.android.myeyepetizer.multitype.FoundCategoryItemBinder;
import com.android.myeyepetizer.multitype.GreyArea;
import com.android.myeyepetizer.multitype.GreyAreaBinder;
import com.android.myeyepetizer.multitype.GreyLineBinder;
import com.android.myeyepetizer.multitype.Line;
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
import okhttp3.Callback;
import retrofit2.http.GET;

public class FollowFragment extends Fragment {

    public static final FollowFragment newInstance() {
        return new FollowFragment();
    }

    private Button mAllAuthorButton;
    private ImageButton mSearchButton;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private MultiTypeAdapter mAdapter;
    private Items mItems;
    private FollowApi mFollowApi;
    private boolean mIsLoading;
    private String mNextPageUrl;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private Observer<GetDataBean> mObserver = new Observer<GetDataBean>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            mCompositeDisposable.add(d);
        }

        @Override
        public void onNext(@NonNull GetDataBean getDataBean) {
            DataPreference.setLastPrefFollowData(getActivity(), new Gson().toJson(getDataBean));
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
        View view = inflater.inflate(R.layout.fragment_follow_layout, container, false);
        initToolbar(view);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataByNet();
            }
        });
        initRecyclerView(view);
        loadData();
        return view;
    }

    private void loadData() {
        String data = null;
        if ((data = DataPreference.getLastPrefFollowData(getActivity())) != null) {
            GetDataBean getDataBean = new Gson().fromJson(data, GetDataBean.class);
            solveData(getDataBean);
        } else {
            loadDataByNet();
        }
        setRecyclerViewSrollListener();
    }

    private void loadDataByNet() {
        mFollowApi = RetrofitFactory.getRetrofit().createApi(FollowApi.class);
        Observable<GetDataBean> observable = mFollowApi.getFollowData();
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
        for (Item item : getDataBean.itemList) {
            switch (item.type) {
                case "videoCollectionWithBrief":
                    mItems.add(new AuthorCollectionItem(item.data, AuthorCollectionItemBinder.FLAG_INTENT_AUTHOR));
                    break;
                case "videoCollectionOfHorizontalScrollCard":
                    mItems.add(new Line());
                    mItems.add(new GreyArea());
                    mItems.add(new FoundCategoryItem(item.data));
                    mItems.add(new Line());
                    mItems.add(new GreyArea());
                    break;
                default:
                    break;
            }
        }
        mAdapter.notifyDataSetChanged();
        mNextPageUrl = getDataBean.nextPageUrl;
        mIsLoading = false;
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
                    if (mFollowApi == null) {
                        mFollowApi = RetrofitFactory.getRetrofit().createApi(FollowApi.class);
                    }
                    Observable<GetDataBean> observable = mFollowApi.loadMoreData(mNextPageUrl);
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
                }
            }
        });
        delegate.attach(mRecyclerView);
    }

    private void initRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.follow_recycler_view);
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(Line.class, new GreyLineBinder());
        mAdapter.register(AuthorCollectionItem.class, new AuthorCollectionItemBinder(getActivity()));
        mAdapter.register(FoundCategoryItem.class, new FoundCategoryItemBinder(getActivity()));
        mAdapter.register(GreyArea.class, new GreyAreaBinder());
        mAdapter.register(EndArea.class, new EndAreaBinder(EndAreaBinder.FLAG_TEXT_COLOR_BLACK));

        mItems = new Items();
        mAdapter.setItems(mItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initToolbar(View view) {
        mSearchButton = (ImageButton) view.findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.fake_anim);
            }
        });

        mAllAuthorButton = (Button) view.findViewById(R.id.tool_bar_button);
        mAllAuthorButton.setText("全部作者");
        mAllAuthorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecyclerViewActivity.class);
                intent.putExtra(RecyclerViewActivity.INTENT_FLAG, RecyclerViewActivity.FLAG_ALL_AUTHOR);
                getActivity().startActivity(intent);
            }
        });

        TextView title = (TextView) view.findViewById(R.id.title_text);
        title.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), getActivity().getString(R.string.LobsterFontPath)));
    }

}
