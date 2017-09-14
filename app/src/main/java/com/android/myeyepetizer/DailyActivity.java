package com.android.myeyepetizer;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.myeyepetizer.Api.DailyApi;
import com.android.myeyepetizer.gson.Issue;
import com.android.myeyepetizer.gson.IssueDataBean;
import com.android.myeyepetizer.gson.Item;
import com.android.myeyepetizer.multitype.Banner;
import com.android.myeyepetizer.multitype.BannerBinder;
import com.android.myeyepetizer.multitype.VideoItem;
import com.android.myeyepetizer.multitype.VideoItemBinder;
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

public class DailyActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    private String mNextPageUrl;
    private boolean mIsLoading;
    private MultiTypeAdapter mAdapter;
    private Items mItems;
    private DailyApi mDailyApi;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private Observer<IssueDataBean> mObserver = new Observer<IssueDataBean>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            mCompositeDisposable.add(d);
        }

        @Override
        public void onNext(@NonNull IssueDataBean issueDataBean) {
            for (Issue issue : issueDataBean.issueList) {
                for (Item item : issue.itemList) {
                    switch (item.type) {
                        case "banner2":
                            mItems.add(new Banner(item.data));
                            break;
                        case "video":
                            mItems.add(new VideoItem(item.data));
                            break;
                        case "textHeader":
                            mItems.add(new TextHeader(item.data.text));
                            break;
                        default:
                            break;
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
            mNextPageUrl = issueDataBean.nextPageUrl;
            mIsLoading = false;
            setRecyclerViewScrollListener();
        }

        @Override
        public void onError(@NonNull Throwable e) {
            Toast.makeText(DailyActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
            mIsLoading = false;
        }

        @Override
        public void onComplete() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_recycler_view);
        initToolbar();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        initRecyclerView();
        loadData();
    }

    private void initToolbar() {
        Button mToolbarButton = (Button) findViewById(R.id.tool_bar_button);
        mToolbarButton.setVisibility(View.GONE);
        ImageButton mSearchButton = (ImageButton) findViewById(R.id.search_button);
        mSearchButton.setVisibility(View.GONE);

        TextView mTitleText = (TextView) findViewById(R.id.title_text);
        mTitleText.setText("每日编辑精选");
        mTitleText.setTextSize(16);
        mTitleText.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.DB1FontPath)));

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        ImageButton mExitButton = new ImageButton(this);
        mExitButton.setBackgroundResource(R.drawable.ic_action_back_black);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.tool_bar_layout);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(100, 120);
        params.gravity = Gravity.CENTER_VERTICAL;
        params.leftMargin = 10;
        frameLayout.addView(mExitButton, params);

        setSupportActionBar(mToolbar);
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initRecyclerView(){
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(Banner.class, new BannerBinder(this));
        mAdapter.register(VideoItem.class, new VideoItemBinder(this));
        mAdapter.register(TextHeader.class, new TextHeaderBinder(TextHeaderBinder.FLAG_HOME));

        mItems = new Items();
        mAdapter.setItems(mItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadData() {
        mDailyApi = RetrofitFactory.getRetrofit().createApi(DailyApi.class);
        Observable<IssueDataBean> observable = mDailyApi.getDailyItem();
        mIsLoading = true;
        observable
                .filter(new Predicate<IssueDataBean>() {
                    @Override
                    public boolean test(@NonNull IssueDataBean issueDataBean) throws Exception {
                        return issueDataBean != null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }

    private void setRecyclerViewScrollListener() {
        LoadMoreDelegate delegate = new LoadMoreDelegate(new LoadMoreDelegate.LoadMoreSubject() {
            @Override
            public boolean isLoading() {
                return mIsLoading;
            }

            @Override
            public void onLoadMore() {
                Observable<IssueDataBean> observable = mDailyApi.loadMoreDailyItem(mNextPageUrl);
                mIsLoading = true;
                observable
                        .filter(new Predicate<IssueDataBean>() {
                            @Override
                            public boolean test(@NonNull IssueDataBean issueDataBean) throws Exception {
                                return issueDataBean != null;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(mObserver);
            }
        });
        delegate.attach(mRecyclerView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
    }
}
