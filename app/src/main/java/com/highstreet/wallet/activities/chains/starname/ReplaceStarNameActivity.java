package com.highstreet.wallet.activities.chains.starname;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.highstreet.wallet.R;
import com.highstreet.wallet.activities.PasswordCheckActivity;
import com.highstreet.wallet.base.BaseActivity;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseFragment;
import com.highstreet.wallet.fragment.chains.starname.ReplaceStarName0Fragment;
import com.highstreet.wallet.fragment.chains.starname.ReplaceStarName1Fragment;
import com.highstreet.wallet.fragment.chains.starname.ReplaceStarName2Fragment;
import com.highstreet.wallet.fragment.chains.starname.ReplaceStarName3Fragment;
import com.highstreet.wallet.model.StarNameDomain;
import com.highstreet.wallet.model.StarNameResource;
import com.highstreet.wallet.model.type.Fee;
import com.highstreet.wallet.network.res.ResIovStarNameResolve;
import com.highstreet.wallet.task.FetchTask.StarNameDomainInfoTask;
import com.highstreet.wallet.task.FetchTask.StarNameResolveTask;
import com.highstreet.wallet.task.TaskResult;

import java.util.ArrayList;

import static com.highstreet.wallet.base.BaseConstant.CONST_PW_PURPOSE;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_REPLACE_STARNAME;
import static com.highstreet.wallet.base.BaseConstant.TASK_FETCH_STARNAME_DOMAIN_INFO;
import static com.highstreet.wallet.base.BaseConstant.TASK_FETCH_STARNAME_RESOLVE;

public class ReplaceStarNameActivity extends BaseActivity {

    private RelativeLayout  mRootView;
    private Toolbar         mToolbar;
    private TextView        mTitle;
    private ImageView       mIvStep;
    private TextView        mTvStep;
    private ViewPager       mViewPager;

    private ReplaceStarNamePageAdapter          mPageAdapter;
    public StarNameDomain                       mStarNameDomain;
    public ResIovStarNameResolve.NameAccount    mMyNameAccount;

    public boolean                      mIsDomain;
    public String                       mToReplaceDomain;
    public String                       mToReplaceAccount;
    public ArrayList<StarNameResource>  mResources = new ArrayList();
    public String                       mMemo;
    public Fee                          mFee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        setContentView(R.layout.activity_step);
        mRootView   = findViewById(R.id.root_view);
        mToolbar    = findViewById(R.id.tool_bar);
        mTitle      = findViewById(R.id.toolbar_title);
        mIvStep     = findViewById(R.id.send_step);
        mTvStep     = findViewById(R.id.send_step_msg);
        mViewPager  = findViewById(R.id.view_pager);
        mTitle.setText(getString(R.string.str_replace_starname));

        mIsDomain = getIntent().getBooleanExtra("IsDomain", false);
        mToReplaceDomain = getIntent().getStringExtra("ToReplaceDomain");
        mToReplaceAccount = getIntent().getStringExtra("ToReplaceAccount");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mIvStep.setImageDrawable(getDrawable(R.drawable.step_4_img_1));
        mTvStep.setText(getString(R.string.str_replace_starname_step_0));

        mAccount = getBaseDao().onSelectAccount(getBaseDao().getLastUser());
        mBaseChain = BaseChain.getChain(mAccount.baseChain);

        mPageAdapter = new ReplaceStarNamePageAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mPageAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) { }

            @Override
            public void onPageSelected(int i) {
                if(i == 0) {
                    mIvStep.setImageDrawable(getDrawable(R.drawable.step_4_img_1));
                    mTvStep.setText(getString(R.string.str_replace_starname_step_0));
                    mPageAdapter.mCurrentFragment.onRefreshTab();
                } else if (i == 1 ) {
                    mIvStep.setImageDrawable(getDrawable(R.drawable.step_4_img_2));
                    mTvStep.setText(getString(R.string.str_replace_starname_step_1));
                } else if (i == 2 ) {
                    mIvStep.setImageDrawable(getDrawable(R.drawable.step_4_img_3));
                    mTvStep.setText(getString(R.string.str_replace_starname_step_2));
                    mPageAdapter.mCurrentFragment.onRefreshTab();
                } else if (i == 3 ) {
                    mIvStep.setImageDrawable(getDrawable(R.drawable.step_4_img_4));
                    mTvStep.setText(getString(R.string.str_replace_starname_step_3));
                    mPageAdapter.mCurrentFragment.onRefreshTab();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) { }
        });
        mViewPager.setCurrentItem(0);

        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHideKeyboard();
            }
        });

        onShowWaitDialog();
        onFetchData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        onHideKeyboard();
        if(mViewPager.getCurrentItem() > 0) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
        } else {
            super.onBackPressed();
        }
    }

    public void onNextStep() {
        if(mViewPager.getCurrentItem() < mViewPager.getChildCount()) {
            onHideKeyboard();
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
        }
    }

    public void onBeforeStep() {
        if(mViewPager.getCurrentItem() > 0) {
            onHideKeyboard();
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
        } else {
            onBackPressed();
        }
    }

    public void onFetchData() {
        mTaskCount = 2;
        new StarNameDomainInfoTask(getBaseApplication(), this, mBaseChain, mToReplaceDomain).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        if (mIsDomain) {
            new StarNameResolveTask(getBaseApplication(), this, mBaseChain, "*" + mToReplaceDomain).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new StarNameResolveTask(getBaseApplication(), this, mBaseChain, mToReplaceAccount + "*" + mToReplaceDomain).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

    }

    public void onStartReplaceResource() {
        Intent intent = new Intent(ReplaceStarNameActivity.this, PasswordCheckActivity.class);
        intent.putExtra(CONST_PW_PURPOSE, CONST_PW_TX_REPLACE_STARNAME);
        intent.putExtra("domain", mToReplaceDomain);
        intent.putExtra("name", TextUtils.isEmpty(mToReplaceAccount) ? "" : mToReplaceAccount);
        intent.putExtra("resource", mResources);
        intent.putExtra("memo", mMemo);
        intent.putExtra("fee", mFee);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out);
    }



    @Override
    public void onTaskResponse(TaskResult result) {
        mTaskCount--;
        if (isFinishing()) return;
        if (result.taskType == TASK_FETCH_STARNAME_DOMAIN_INFO) {
            if (result.isSuccess) {
                mStarNameDomain = (StarNameDomain)result.resultData;
            }

        } else if (result.taskType == TASK_FETCH_STARNAME_RESOLVE) {
            if (result.isSuccess) {
                mMyNameAccount = (ResIovStarNameResolve.NameAccount)result.resultData;
            }

        }
        if (mTaskCount == 0) {
            onHideWaitDialog();
            mPageAdapter.mCurrentFragment.onRefreshTab();
        }
    }


    private class ReplaceStarNamePageAdapter extends FragmentPagerAdapter {

        private ArrayList<BaseFragment> mFragments = new ArrayList<>();
        private BaseFragment mCurrentFragment;

        public ReplaceStarNamePageAdapter(FragmentManager fm) {
            super(fm);
            mFragments.clear();
            mFragments.add(ReplaceStarName0Fragment.newInstance(null));
            mFragments.add(ReplaceStarName1Fragment.newInstance(null));
            mFragments.add(ReplaceStarName2Fragment.newInstance(null));
            mFragments.add(ReplaceStarName3Fragment.newInstance(null));
        }

        @Override
        public BaseFragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object) {
                mCurrentFragment = ((BaseFragment) object);
            }
            super.setPrimaryItem(container, position, object);
        }

        public BaseFragment getCurrentFragment() {
            return mCurrentFragment;
        }

        public ArrayList<BaseFragment> getFragments() {
            return mFragments;
        }

    }
}
