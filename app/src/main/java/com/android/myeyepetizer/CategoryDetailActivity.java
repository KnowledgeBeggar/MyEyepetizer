package com.android.myeyepetizer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.myeyepetizer.Api.CategoryApi;
import com.android.myeyepetizer.adpter.FragmentAdapter;
import com.android.myeyepetizer.gson.CategoryDataBean;
import com.android.myeyepetizer.gson.Tab;
import com.android.myeyepetizer.utils.AddSpaceToString;
import com.android.myeyepetizer.view.CategoryViewPagerFragment;
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

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class CategoryDetailActivity extends BaseActivity {

    public static final String INTENT_ID_KEY = "ID";

    private CollapsingToolbarLayout mToolbarLayout;
    private Toolbar mToolbar;
    private ImageView mBackground;
    private TextView mCategoryName;
    private TextView mDescription;
    private MagicIndicator mMagicIndicator;
    private ViewPager mViewPager;

    private CategoryApi mCategoryApi;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private List<Tab> mAllTabs;
    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        mId = getIntent().getIntExtra(INTENT_ID_KEY, 2);
        initToolBar();
        loadData();
    }

    private void initToolBar() {
        mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_tool_bar);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarLayout.setCollapsedTitleTextAppearance(R.style.ToolbarTitileCollapsed);
        mToolbarLayout.setCollapsedTitleTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.DB1FontPath)));

        mBackground = (ImageView) findViewById(R.id.background);

        mCategoryName = (TextView) findViewById(R.id.category_name);
        mCategoryName.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.DB1FontPath)));

        mDescription = (TextView) findViewById(R.id.description);
        mDescription.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.LFontPath)));
    }

    private void loadData() {
        mCategoryApi = RetrofitFactory.getRetrofit().createApi(CategoryApi.class);
        Observable<CategoryDataBean> observable = mCategoryApi.getCategoryData(mId);
        observable
                .filter(new Predicate<CategoryDataBean>() {
                    @Override
                    public boolean test(@NonNull CategoryDataBean categoryDataBean) throws Exception {
                        return categoryDataBean != null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CategoryDataBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull CategoryDataBean categoryDataBean) {
                        Glide.with(CategoryDetailActivity.this).load(categoryDataBean.categoryInfo.headerImage).into(mBackground);

                        mToolbarLayout.setTitle(categoryDataBean.categoryInfo.name);
                        mCategoryName.setText(categoryDataBean.categoryInfo.name);
                        mDescription.setText(categoryDataBean.categoryInfo.description);
                        mAllTabs = categoryDataBean.tabInfo.tabList;
                        initViewPager();
                        initMagicIndicator();
                        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(CategoryDetailActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        List<Fragment> fragments = new ArrayList<>();
        for (Tab tab : mAllTabs) {
            CategoryViewPagerFragment fragment = CategoryViewPagerFragment.newInstance();
            fragment.setTab(tab);
            fragments.add(fragment);
        }

        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragments, null));
    }

    private void initMagicIndicator() {
        mMagicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
        CommonNavigator navigator = new CommonNavigator(CategoryDetailActivity.this);
        navigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mAllTabs != null ? mAllTabs.size() : 0;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int i) {
                SimplePagerTitleView titleView = new SimplePagerTitleView(context);
                titleView.setText(mAllTabs.get(i).name);
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
        mMagicIndicator.setNavigator(navigator);
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
