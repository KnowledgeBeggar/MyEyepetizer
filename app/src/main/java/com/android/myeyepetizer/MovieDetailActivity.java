package com.android.myeyepetizer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.myeyepetizer.Api.RelateListApi;
import com.android.myeyepetizer.gson.Data;
import com.android.myeyepetizer.gson.GetDataBean;
import com.android.myeyepetizer.gson.Item;
import com.android.myeyepetizer.listener.SampleListener;
import com.android.myeyepetizer.multitype.GreyLineBinder;
import com.android.myeyepetizer.multitype.Line;
import com.android.myeyepetizer.multitype.MovieDetailAuthor;
import com.android.myeyepetizer.multitype.MovieDetailAuthorBinder;
import com.android.myeyepetizer.multitype.MovieDetailCategoryBinder;
import com.android.myeyepetizer.multitype.Category;
import com.android.myeyepetizer.multitype.EndArea;
import com.android.myeyepetizer.multitype.EndAreaBinder;
import com.android.myeyepetizer.multitype.MovieDetailRelateItem;
import com.android.myeyepetizer.multitype.MovieDetailRelateItemBinder;
import com.android.myeyepetizer.multitype.MovieInfo;
import com.android.myeyepetizer.multitype.MovieInfoBinder;
import com.android.myeyepetizer.view.ReplyFragment;
import com.bumptech.glide.Glide;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.listener.StandardVideoAllCallBack;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class MovieDetailActivity extends BaseActivity {

    private StandardGSYVideoPlayer mPlayer;
    private MultiTypeAdapter mAdapter;
    private Items mItems;
    private Data mData;
    private Disposable mDisposable;
    private int mMovieId;
    private OrientationUtils mOrientationUtils;
    private boolean mIsPause;
    private boolean mIsPlay;

    private ImageView mVideoImageView;
    private ImageView mBackgroundImageView;
    private ImageButton mReplyButton;
    private RecyclerView mRecyclerView;
    private RelateListApi mRelatedDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mData = bundle.getParcelable("DATA");
        mMovieId = mData.id;
        mItems = new Items();
        mBackgroundImageView = (ImageView) findViewById(R.id.background);
        mPlayer = (StandardGSYVideoPlayer) findViewById(R.id.video_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.movie_recycler_view);
        mRelatedDatas = RetrofitFactory.getRetrofit().createApi(RelateListApi.class);

        initVideoPlayer();
        initRecycler();
        loadData();
    }

    private void initVideoPlayer() {
        String image = mData.cover.detailCover;
        String video = mData.playUrl;

        mVideoImageView = new ImageView(this);
        mVideoImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this).load(image).into(mVideoImageView);

        resolveNormalVideoUI();
        mOrientationUtils = new OrientationUtils(this, mPlayer);
        mOrientationUtils.setEnable(false); //初始化不打开外部旋转

        GSYVideoOptionBuilder builder = new GSYVideoOptionBuilder();
        builder.setThumbImageView(mVideoImageView)
                .setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setUrl(video)
                .setCacheWithPlay(false)
                .setVideoTitle(mData.title)
                .setStandardVideoAllCallBack(new SampleListener() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        //开始播放了才能旋转和全屏
                        mOrientationUtils.setEnable(true);
                        mIsPlay = true;
                    }

                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                    }

                    @Override
                    public void onAutoComplete(String url, Object... objects) {
                        super.onAutoComplete(url, objects);
                    }

                    @Override
                    public void onClickStartError(String url, Object... objects) {
                        super.onClickStartError(url, objects);
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        if (mOrientationUtils != null) {
                            mOrientationUtils.backToProtVideo();
                        }
                    }
                })
                .setLockClickListener(new LockClickListener() {
                    @Override
                    public void onClick(View view, boolean lock) {
                        if (mOrientationUtils != null) {
                            mOrientationUtils.setEnable(!lock);
                        }
                    }
                })
                .build(mPlayer);
        mPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrientationUtils.resolveByClick();
                mPlayer.startWindowFullscreen(MovieDetailActivity.this, true, true);
            }
        });
    }

    private void initRecycler() {
        Glide.with(this).load(mData.cover.blurredCover).into(mBackgroundImageView);
        initMultitypeAdpter();
        initItems();

        mAdapter.setItems(mItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initMultitypeAdpter() {
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(MovieInfo.class, new MovieInfoBinder());
        mAdapter.register(Line.class, new GreyLineBinder());
        mAdapter.register(MovieDetailAuthor.class, new MovieDetailAuthorBinder(MovieDetailActivity.this));
        mAdapter.register(Category.class, new MovieDetailCategoryBinder());
        mAdapter.register(MovieDetailRelateItem.class, new MovieDetailRelateItemBinder(this, MovieDetailRelateItemBinder.FLAG_WHITE_WORD_COLOR));
        mAdapter.register(EndArea.class, new EndAreaBinder(EndAreaBinder.FLAG_TEXT_COLOR_WHITE));
    }

    private void initItems() {
        mItems.clear();
        mItems.add(new MovieInfo(mData.title, mData.category, mData.description, mData.duration,
                mData.consumption.collectionCount, mData.consumption.shareCount, mData.consumption.replyCount, true));
        mItems.add(new Line());
        if (mData.author != null) {
            mItems.add(new MovieDetailAuthor(mData.author.icon, mData.author.name, mData.author.description, mData.author.id));
            mItems.add(new Line());
        }
        mAdapter.notifyDataSetChanged();
    }

    private void loadData() {
        Observable<GetDataBean> dataBean = mRelatedDatas.getRelatedData(mData.id);
        dataBean
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
                        notifyAdapter(getDataBean.itemList);

                        final FragmentManager manager = getSupportFragmentManager();
                        mReplyButton = (ImageButton) findViewById(R.id.reply_button);
                        mReplyButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                manager.beginTransaction()
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                        .add(R.id.fragment_container, ReplyFragment.newInstance(), "REPLY")
                                        .commit();
                                mRecyclerView.setVisibility(View.INVISIBLE);
                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void notifyAdapter(List<Item> itemList) {
        for (Item item : itemList) {
            if (item.type.equals("textCard")) {
                mItems.add(new Category(item.data.text));
            } else if (item.type.equals("videoSmallCard")) {
                mItems.add(new MovieDetailRelateItem(item.data));
            }
        }
        mItems.add(new EndArea());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (mOrientationUtils != null) {
            mOrientationUtils.backToProtVideo();
        }
        if (mPlayer.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsPause = false;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fake_anim, R.anim.slide_out_down);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoPlayer.releaseAllVideos();
        mDisposable.dispose();
        if (mOrientationUtils != null) {
            mOrientationUtils.releaseListener();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (mIsPlay && !mIsPause) {
            if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
                if (!mPlayer.isIfCurrentIsFullscreen()) {
                    mPlayer.startWindowFullscreen(MovieDetailActivity.this, true, true);
                }
            } else {
                //新版本isIfCurrentIsFullscreen的标志位内部提前设置了，所以不会和手动点击冲突
                if (mPlayer.isIfCurrentIsFullscreen()) {
                    StandardGSYVideoPlayer.backFromWindowFull(this);
                }
                if (mOrientationUtils != null) {
                    mOrientationUtils.setEnable(true);
                }
            }
        }
    }


    public void updateData(Data data) {
        mData = data;
        mPlayer.clearThumbImageView();
        initVideoPlayer();

        Glide.with(this).load(mData.cover.blurredCover).into(mBackgroundImageView);
        initItems();
        loadData();
        mRecyclerView.getLayoutManager().scrollToPosition(0);
    }

    public void setRecyclerViewVisible() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public int getMovieId() {
        return mMovieId;
    }

    private void resolveNormalVideoUI() {
        mPlayer.getTitleTextView().setVisibility(View.GONE);
        mPlayer.getBackButton().setVisibility(View.GONE);
    }

}
