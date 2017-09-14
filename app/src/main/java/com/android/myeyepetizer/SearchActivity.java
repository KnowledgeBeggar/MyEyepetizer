package com.android.myeyepetizer;

import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.myeyepetizer.Api.SearchApi;
import com.android.myeyepetizer.gson.GetDataBean;
import com.android.myeyepetizer.utils.AddSpaceToString;
import com.android.myeyepetizer.view.NoSearchDataFragment;
import com.android.myeyepetizer.view.SearchFragment;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{

    private FragmentManager mFragmentManager;
    private TagFlowLayout mTagFlowLayout;
    private ProgressBar mProgressBar;
    private TextView mHintText;
    private LinearLayout mTagLayout;
    private SearchView mSearchView;
    private Button mExitButton;
    private FrameLayout mFragmentContainer;

    private List<String> mTags;
    private String mSearchTag;
    private SearchApi mSearchApi;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Observer<GetDataBean> mObserver = new Observer<GetDataBean>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            mCompositeDisposable.add(d);
        }

        @Override
        public void onNext(@NonNull GetDataBean getDataBean) {
            if (getDataBean.total != 0) {
                SearchFragment fragment = SearchFragment.newInstance(getDataBean);
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
                mHintText.setText("- [" + mSearchTag + "] 搜索结果共" + getDataBean.total + "个 -");
                mHintText.setTextColor(ContextCompat.getColor(SearchActivity.this, R.color.colorBlack));
                mHintText.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.DB1FontPath)));
            } else {
                NoSearchDataFragment fragment = NoSearchDataFragment.newInstance();
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }
            mProgressBar.setVisibility(View.GONE);
            mFragmentContainer.setVisibility(View.VISIBLE);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            Toast.makeText(SearchActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mTagFlowLayout = (TagFlowLayout) findViewById(R.id.tag_flow_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mHintText = (TextView) findViewById(R.id.hint_text);
        mTagLayout = (LinearLayout) findViewById(R.id.tag_layout);
        mFragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mFragmentContainer.setVisibility(View.GONE);
                searchData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mExitButton = (Button) findViewById(R.id.exit_button);
        mExitButton.setOnClickListener(this);
        mFragmentManager = getSupportFragmentManager();
        loadData();
    }

    private void loadData() {
        mSearchApi = RetrofitFactory.getRetrofit().createApi(SearchApi.class);
        Observable<List<String>> observable = mSearchApi.getHotTags();
        observable
                .filter(new Predicate<List<String>>() {
                    @Override
                    public boolean test(@NonNull List<String> list) throws Exception {
                        return list != null;
                    }
                })
                .doOnNext(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> list) throws Exception {
                        mTags = list;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull List<String> list) {
                        bindData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(SearchActivity.this, "加载失败", Toast.LENGTH_SHORT);
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void bindData() {
        mTagFlowLayout.setAdapter(new TagAdapter<String>(mTags) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(SearchActivity.this).inflate(R.layout.layout_hot_tag, mTagFlowLayout, false);
                tv.setText(AddSpaceToString.addSpace(s));
                return tv;
            }
        });
        mTagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, final int position, FlowLayout parent) {
                searchData(mTags.get(position));
                return true;
            }
        });
    }

    private void searchData(String tag) {
        mHintText.setText("");
        mTagLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mSearchTag = tag;
        Observable<GetDataBean> observable = mSearchApi.searchTagData(mSearchTag);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit_button:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fake_anim, R.anim.slide_out_down);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
    }
}
