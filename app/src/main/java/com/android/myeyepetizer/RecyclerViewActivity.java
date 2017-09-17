package com.android.myeyepetizer;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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

import com.android.myeyepetizer.Api.CategoryApi;
import com.android.myeyepetizer.Api.FoundApi;
import com.android.myeyepetizer.Api.SubjectApi;
import com.android.myeyepetizer.gson.GetDataBean;
import com.android.myeyepetizer.gson.Item;
import com.android.myeyepetizer.multitype.AuthorCard;
import com.android.myeyepetizer.multitype.AuthorCardBinder;
import com.android.myeyepetizer.multitype.AuthorCollectionItem;
import com.android.myeyepetizer.multitype.AuthorCollectionItemBinder;
import com.android.myeyepetizer.multitype.Banner;
import com.android.myeyepetizer.multitype.BannerBinder;
import com.android.myeyepetizer.multitype.EndArea;
import com.android.myeyepetizer.multitype.EndAreaBinder;
import com.android.myeyepetizer.multitype.LeftAlignTextHeader;
import com.android.myeyepetizer.multitype.LeftAlignTextHeaderBinder;
import com.android.myeyepetizer.multitype.SquareCardBinder;
import com.android.myeyepetizer.utils.LoadMoreDelegate;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.ClassLinker;
import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class RecyclerViewActivity extends BaseActivity {

    public static final String INTENT_FLAG = "IntentFlag";
    public static final int FLAG_ALL_CATEGORY = 0;
    public static final int FLAG_HOT_SUBJECT = 1;
    public static final int FLAG_ALL_AUTHOR = 2;

    private static final String TITLE_CATEGORY = "全部分类";
    private static final String TITLE_SUBJECT = "专题";
    private static final String TITLE_AUTHOR = "全部作者";
    private static final String ALL_AUTHOR_DATA_URL = "http://baobab.kaiyanapp.com/api/v4/pgcs/all";

    private Toolbar mToolbar;
    private TextView mTitleText;
    private RecyclerView mRecyclerView;
    private MultiTypeAdapter mAdapter;
    private Items mItems;
    private CategoryApi mCategoryApi;
    private SubjectApi mSubjectApi;
    private FoundApi mFoundApi;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private int mFlag;
    private String mNextPageUrl;
    private boolean mIsLoading;

    private Observer<GetDataBean> mObserver = new Observer<GetDataBean>() {
        @Override
        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
            mCompositeDisposable.add(d);
        }

        @Override
        public void onNext(@io.reactivex.annotations.NonNull GetDataBean getDataBean) {
            for (Item item : getDataBean.itemList) {
                switch (item.type) {
                    case "banner2":
                        mItems.add(new Banner(item.data, -1, FLAG_HOT_SUBJECT));
                        break;
                    case "squareCard":
                        mItems.add(new Banner(item.data, SquareCardBinder.FLAG_START_CATEGORY_ACTIVITY, FLAG_ALL_CATEGORY));
                        break;
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
            mAdapter.notifyDataSetChanged();
            mNextPageUrl = getDataBean.nextPageUrl;
            mIsLoading = false;
        }

        @Override
        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
            Toast.makeText(RecyclerViewActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
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
        mFlag = getIntent().getIntExtra(INTENT_FLAG, 0);
        initToolbar(mFlag);
        initRecyclerView(mFlag);
        loadData(mFlag);
    }

    private void loadData(int flag) {
        if (flag == FLAG_ALL_CATEGORY) {
            loadAllCategoryData();
        } else if (flag == FLAG_HOT_SUBJECT) {
            loadHotSubjectData();
        } else if (flag == FLAG_ALL_AUTHOR) {
            loadAllAuthorData();
        }
        setRecyclerViewScrollListener();
    }

    private void loadAllCategoryData() {
        mCategoryApi = RetrofitFactory.getRetrofit().createApi(CategoryApi.class);
        Observable<GetDataBean> observable = mCategoryApi.getAllCategoryData();
        observable
                .filter(new Predicate<GetDataBean>() {
                    @Override
                    public boolean test(@io.reactivex.annotations.NonNull GetDataBean getDataBean) throws Exception {
                        return getDataBean != null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }

    private void loadHotSubjectData() {
        mSubjectApi = RetrofitFactory.getRetrofit().createApi(SubjectApi.class);
        Observable<GetDataBean> observable = mSubjectApi.getHotSubject();
        observable
                .filter(new Predicate<GetDataBean>() {
                    @Override
                    public boolean test(@io.reactivex.annotations.NonNull GetDataBean getDataBean) throws Exception {
                        return getDataBean != null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }

    private void loadAllAuthorData() {
        mFoundApi = RetrofitFactory.getRetrofit().createApi(FoundApi.class);
        Observable<GetDataBean> observable = mFoundApi.getFoundCategoryData(ALL_AUTHOR_DATA_URL);
        observable
                .filter(new Predicate<GetDataBean>() {
                    @Override
                    public boolean test(@io.reactivex.annotations.NonNull GetDataBean getDataBean) throws Exception {
                        return getDataBean != null;
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
                mIsLoading = true;
                if (mNextPageUrl != null) {
                    Observable<GetDataBean> observable = null;
                    if (mFlag == FLAG_HOT_SUBJECT) {
                        observable = mSubjectApi.loadMoreSubject(mNextPageUrl);
                    } else if (mFlag == FLAG_ALL_AUTHOR) {
                        observable = mFoundApi.getFoundCategoryData(mNextPageUrl);
                    }
                    observable
                            .filter(new Predicate<GetDataBean>() {
                                @Override
                                public boolean test(@io.reactivex.annotations.NonNull GetDataBean getDataBean) throws Exception {
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

    private void initRecyclerView(int flag) {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(Banner.class)
                .to(new BannerBinder(this), new SquareCardBinder(this))
                .withClassLinker(new ClassLinker<Banner>() {
                    @NonNull
                    @Override
                    public Class<? extends ItemViewBinder<Banner, ?>> index(@NonNull Banner banner) {
                        if (banner.type == FLAG_ALL_CATEGORY) {
                            return SquareCardBinder.class;
                        } else {
                            return BannerBinder.class;
                        }
                    }
                });
        mAdapter.register(EndArea.class, new EndAreaBinder(EndAreaBinder.FLAG_TEXT_COLOR_BLACK));
        mAdapter.register(LeftAlignTextHeader.class, new LeftAlignTextHeaderBinder());
        mAdapter.register(AuthorCard.class, new AuthorCardBinder(this));
        mAdapter.register(AuthorCollectionItem.class, new AuthorCollectionItemBinder(this));

        mItems = new Items();
        mAdapter.setItems(mItems);
        if (flag == FLAG_ALL_CATEGORY) {
            GridLayoutManager manager = new GridLayoutManager(this, 2);
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (mItems.get(position) instanceof EndArea) ? 2 : 1;
                }
            });
            mRecyclerView.setLayoutManager(manager);
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initToolbar(int flag) {
        Button mToolbarButton = (Button) findViewById(R.id.tool_bar_button);
        mToolbarButton.setVisibility(View.GONE);
        ImageButton mSearchButton = (ImageButton) findViewById(R.id.search_button);
        mSearchButton.setVisibility(View.GONE);

        mTitleText = (TextView) findViewById(R.id.title_text);
        if (flag == FLAG_ALL_CATEGORY) {
            mTitleText.setText(TITLE_CATEGORY);
        } else if (flag == FLAG_HOT_SUBJECT) {
            mTitleText.setText(TITLE_SUBJECT);
        } else if (flag == FLAG_ALL_AUTHOR) {
            mTitleText.setText(TITLE_AUTHOR);
        }
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fake_anim, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
    }
}
