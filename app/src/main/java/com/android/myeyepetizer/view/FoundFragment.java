package com.android.myeyepetizer.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.myeyepetizer.Api.FoundApi;
import com.android.myeyepetizer.R;
import com.android.myeyepetizer.RecyclerViewActivity;
import com.android.myeyepetizer.RetrofitFactory;
import com.android.myeyepetizer.SearchActivity;
import com.android.myeyepetizer.adpter.FragmentAdapter;
import com.android.myeyepetizer.gson.Discovery;
import com.android.myeyepetizer.gson.Tab;

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

public class FoundFragment extends Fragment {

    public static FoundFragment newInstance() {
        return new FoundFragment();
    }

    private ViewPager mViewPager;
    private MagicIndicator mMagicIndicator;
    private List<String> mTabTitles;
    private List<Fragment> mFragments;
    private List<Tab> mTabList;

    private FoundApi mFoundApi;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_found_layout, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        Button button = (Button) view.findViewById(R.id.all_category_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecyclerViewActivity.class);
                intent.putExtra(RecyclerViewActivity.INTENT_FLAG, RecyclerViewActivity.FLAG_ALL_CATEGORY);
                getActivity().startActivity(intent);
            }
        });

        loadData(view);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(3);

        ImageButton searchButton = (ImageButton) view.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.fake_anim);
            }
        });

        return view;
    }

    private void loadData(final View view) {
        mFoundApi = RetrofitFactory.getRetrofit().createApi(FoundApi.class);
        Observable<Discovery> observable = mFoundApi.getDiscovery();
        observable
                .filter(new Predicate<Discovery>() {
                    @Override
                    public boolean test(@NonNull Discovery discovery) throws Exception {
                        return discovery != null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Discovery>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Discovery discovery) {
                        mTabList = discovery.tabInfo.tabList;
                        initViewPager();
                        initMagicIndicator(view);
                        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initViewPager() {
        mTabTitles = new ArrayList<>();
        mTabTitles.add("热门");
        mTabTitles.add("分类");
        mTabTitles.add("作者");

        mFragments = new ArrayList<>();
        for (Tab tab : mTabList) {
            FoundViewPagerFragment fragment = FoundViewPagerFragment.newInstance();
            fragment.setTab(tab);
            mFragments.add(fragment);
        }
        mViewPager.setAdapter(new FragmentAdapter(getChildFragmentManager(), mFragments, mTabTitles));
    }

    private void initMagicIndicator(final View view) {
        mMagicIndicator = (MagicIndicator) view.findViewById(R.id.magic_indicator);
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
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
