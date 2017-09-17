package com.android.myeyepetizer;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.myeyepetizer.Api.SubjectApi;
import com.android.myeyepetizer.gson.Item;
import com.android.myeyepetizer.gson.WebPageData;
import com.android.myeyepetizer.multitype.GreyArea;
import com.android.myeyepetizer.multitype.GreyAreaBinder;
import com.android.myeyepetizer.multitype.SubjectHeader;
import com.android.myeyepetizer.multitype.SubjectHeaderBinder;
import com.android.myeyepetizer.multitype.SubjectItemBinder;
import com.android.myeyepetizer.multitype.VideoItem;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class SubjectActivity extends BaseActivity {

    private TextView mTitleText;
    private ImageButton mExitButton;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private MultiTypeAdapter mAdapter;
    private Items mItems;
    private SubjectApi mSubjectApi;
    private Disposable mDisposable;
    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_recycler_view);
        mId = getIntent().getIntExtra("ID", 1);
        initToolbar();
        initRecyclerView();
        loadData();
        Log.d("Sub", mId + "");
    }

    private void loadData() {
        mSubjectApi = RetrofitFactory.getRetrofit().createApi(SubjectApi.class);
        Observable<WebPageData> observable = mSubjectApi.getDetailSubject(mId);
        observable
                .filter(new Predicate<WebPageData>() {
                    @Override
                    public boolean test(@NonNull WebPageData webPageData) throws Exception {
                        return webPageData != null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WebPageData>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull WebPageData webPageData) {
                        mTitleText.setText(webPageData.brief);
                        mItems.add(new SubjectHeader(webPageData.headerImage, webPageData.brief, webPageData.text));
                        for (Item item : webPageData.itemList) {
                            mItems.add(new GreyArea());
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

    private void initToolbar() {
        Button mToolbarButton = (Button) findViewById(R.id.tool_bar_button);
        mToolbarButton.setVisibility(View.GONE);
        ImageButton mSearchButton = (ImageButton) findViewById(R.id.search_button);
        mSearchButton.setVisibility(View.GONE);

        mTitleText = (TextView) findViewById(R.id.title_text);
        mTitleText.setText("");
        mTitleText.setTextSize(16);
        mTitleText.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.DB1FontPath)));

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mExitButton = new ImageButton(this);
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

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(SubjectHeader.class, new SubjectHeaderBinder());
        mAdapter.register(GreyArea.class, new GreyAreaBinder());
        mAdapter.register(VideoItem.class, new SubjectItemBinder(this));

        mItems = new Items();
        mAdapter.setItems(mItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fake_anim, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
        GSYVideoPlayer.releaseAllVideos();
    }
}
