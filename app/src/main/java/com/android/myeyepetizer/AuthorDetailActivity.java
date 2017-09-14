package com.android.myeyepetizer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.myeyepetizer.Api.AuthorApi;
import com.android.myeyepetizer.adpter.FragmentAdapter;
import com.android.myeyepetizer.gson.AuthorDataBean;
import com.android.myeyepetizer.gson.Tab;
import com.android.myeyepetizer.view.AuthorViewPagerFragment;
import com.bumptech.glide.Glide;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class AuthorDetailActivity extends AppCompatActivity {

    public static final String FLAG_INTENT_ID = "ID";

    private CollapsingToolbarLayout mToolbarLayout;
    private Toolbar mToolbar;
    private MagicIndicator mIndicator;
    private CircleImageView mAuthorIcon;
    private ViewPager mViewPager;
    private TextView mAuthorName;
    private TextView mBriefText;
    private TextView mDescription;
    private AuthorApi mAuthorApi;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private int mId;
    private List<Tab> mTabList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_detail);
        mId = getIntent().getIntExtra(FLAG_INTENT_ID, 1);
        initToolbar();
        loadData();
    }

    private void loadData() {
        mAuthorApi = RetrofitFactory.getRetrofit().createApi(AuthorApi.class);
        Observable<AuthorDataBean> observable = mAuthorApi.getAuthorData(mId);
        observable
                .filter(new Predicate<AuthorDataBean>() {
                    @Override
                    public boolean test(@NonNull AuthorDataBean authorDataBean) throws Exception {
                        return authorDataBean != null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AuthorDataBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull AuthorDataBean authorDataBean) {
                        Glide.with(AuthorDetailActivity.this).load(authorDataBean.pgcInfo.icon).into(mAuthorIcon);
                        mAuthorName.setText(authorDataBean.pgcInfo.name);
                        mBriefText.setText(authorDataBean.pgcInfo.brief);
                        mDescription.setText(authorDataBean.pgcInfo.description);
                        mToolbarLayout.setTitle(authorDataBean.pgcInfo.name);
                        mTabList = authorDataBean.tabInfo.tabList;

                        initViewPager();
                        initIndicator();
                        ViewPagerHelper.bind(mIndicator, mViewPager);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(AuthorDetailActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initIndicator() {
        mIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
        CommonNavigator navigator = new CommonNavigator(AuthorDetailActivity.this);
        navigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTabList != null ? mTabList.size() : 0;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int i) {
                SimplePagerTitleView titleView = new SimplePagerTitleView(context);
                titleView.setText(mTabList.get(i).name);
                titleView.setNormalColor(Color.GRAY);
                titleView.setSelectedColor(Color.BLACK);

                titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(i, false);
                    }
                });
                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineWidth(30);

                return indicator;
            }
        });
        navigator.setAdjustMode(true);
        mIndicator.setNavigator(navigator);
    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        List<Fragment> fragments = new ArrayList<>();
        for (Tab tab : mTabList) {
            AuthorViewPagerFragment fragment = AuthorViewPagerFragment.newInstance(tab);
            fragments.add(fragment);
        }

        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragments, null));
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.author_detail_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_tool_bar);
        mToolbarLayout.setCollapsedTitleTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.DB1FontPath)));

        mAuthorIcon = (CircleImageView) findViewById(R.id.author_icon);
        mAuthorName = (TextView) findViewById(R.id.author_name);
        mAuthorName.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.DB1FontPath)));
        mBriefText = (TextView) findViewById(R.id.brief_text);
        mBriefText.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.LFontPath)));
        mDescription = (TextView) findViewById(R.id.description);
        mDescription.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.LFontPath)));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
