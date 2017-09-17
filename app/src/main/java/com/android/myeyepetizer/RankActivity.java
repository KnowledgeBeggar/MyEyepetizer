package com.android.myeyepetizer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.myeyepetizer.adpter.FragmentAdapter;
import com.android.myeyepetizer.view.RankViewPagerFragment;

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

public class RankActivity extends BaseActivity {

    private MagicIndicator mMagicIndicator;
    private ViewPager mViewPager;
    private List<String> mTabTitles;
    private List<Fragment> mFragments;
    private TextView mTitle;
    private ImageButton mExitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        mTitle = (TextView) findViewById(R.id.title_text);
        mTitle.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.LobsterFontPath)));
        mExitButton = (ImageButton) findViewById(R.id.exit_button);
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initViewPager();
        initMagicIndicator();
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
        int index = getIntent().getIntExtra("Category", 1);
        mViewPager.setCurrentItem(index - 1);

    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabTitles = new ArrayList<>();
        mTabTitles.add(RankViewPagerFragment.FLAG_WEEK);
        mTabTitles.add(RankViewPagerFragment.FLAG_MONTH);
        mTabTitles.add(RankViewPagerFragment.FLAG_HISTORICAL);

        mFragments = new ArrayList<>();
        for (String s : mTabTitles) {
            RankViewPagerFragment fragment = RankViewPagerFragment.newInstance();
            fragment.setLoadFlag(s);
            mFragments.add(fragment);
        }
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), mFragments, mTabTitles));
        mViewPager.setOffscreenPageLimit(3);
    }

    private void initMagicIndicator() {
        mMagicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTabTitles == null ? 0 : mTabTitles.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);

                simplePagerTitleView.setText(mTabTitles.get(index));
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.BLACK);

                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index, false);
                    }
                });

                return simplePagerTitleView;

            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                linePagerIndicator.setLineWidth(30);
                return linePagerIndicator;
            }
        });
        commonNavigator.setAdjustMode(true);
        mMagicIndicator.setNavigator(commonNavigator);
    }
}
