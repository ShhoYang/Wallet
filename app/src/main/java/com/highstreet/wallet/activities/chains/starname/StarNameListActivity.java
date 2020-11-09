package com.highstreet.wallet.activities.chains.starname;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.highstreet.wallet.R;
import com.highstreet.wallet.base.BaseActivity;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseFragment;
import com.highstreet.wallet.fragment.chains.starname.MyAccountFragment;
import com.highstreet.wallet.fragment.chains.starname.MyDomainFragment;
import com.highstreet.wallet.model.StarNameAccount;
import com.highstreet.wallet.model.StarNameDomain;
import com.highstreet.wallet.task.FetchTask.StarNameMyAccountTask;
import com.highstreet.wallet.task.FetchTask.StarNameMyDomainTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WDp;
import com.highstreet.wallet.utils.WLog;

import java.util.ArrayList;

import static com.highstreet.wallet.base.BaseConstant.TASK_FETCH_MY_STARNAME_ACCOUNT;
import static com.highstreet.wallet.base.BaseConstant.TASK_FETCH_MY_STARNAME_DOMAIN;

public class StarNameListActivity extends BaseActivity implements TaskListener {

    private Toolbar             mToolbar;
    private TextView            mToolbarTitle;
    private ViewPager           mNameServicePager;
    private TabLayout           mNameServiceTapLayer;
    private StarNamePageAdapter mPageAdapter;

    public ArrayList<StarNameDomain>   mMyStarNameDomains = new ArrayList<>();
    public ArrayList<StarNameAccount>  mMyStarNameAccounts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starname_list);
        mToolbar            = findViewById(R.id.tool_bar);
        mToolbarTitle       = findViewById(R.id.toolbar_title);
        mNameServiceTapLayer  = findViewById(R.id.name_service_tab);
        mNameServicePager     = findViewById(R.id.name_service_view_pager);

        mAccount = getBaseDao().onSelectAccount(getBaseDao().getLastUser());
        mBaseChain = BaseChain.getChain(mAccount.baseChain);

        mPageAdapter = new StarNamePageAdapter(getSupportFragmentManager());
        mNameServicePager.setAdapter(mPageAdapter);
        mNameServiceTapLayer.setupWithViewPager(mNameServicePager);
        mNameServiceTapLayer.setTabRippleColor(null);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        View tab0 = LayoutInflater.from(this).inflate(R.layout.view_tab_myvalidator, null);
        TextView tabItemText0 = tab0.findViewById(R.id.tabItemText);
        tabItemText0.setText(R.string.str_my_domain);
        tabItemText0.setTextColor(WDp.getTabColor(this, mBaseChain));
        mNameServiceTapLayer.getTabAt(0).setCustomView(tab0);

        View tab1 = LayoutInflater.from(this).inflate(R.layout.view_tab_myvalidator, null);
        TextView tabItemText1 = tab1.findViewById(R.id.tabItemText);
        tabItemText1.setTextColor(WDp.getTabColor(this, mBaseChain));
        tabItemText1.setText(R.string.str_my_account);
        mNameServiceTapLayer.getTabAt(1).setCustomView(tab1);

        mNameServiceTapLayer.setTabIconTint(WDp.getChainTintColor(this, mBaseChain));
        mNameServiceTapLayer.setSelectedTabIndicatorColor(WDp.getChainColor(this, mBaseChain));

        mNameServicePager.setOffscreenPageLimit(2);
        mNameServicePager.setCurrentItem(0, false);
        mNameServicePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) { }

            @Override
            public void onPageScrollStateChanged(int i) { }

            @Override
            public void onPageSelected(int i) {
                mPageAdapter.mFragments.get(i).onRefreshTab();
            }
        });

        onShowWaitDialog();
        onFetch();

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


    public void onFetch() {
        if (mTaskCount > 0) {
            mPageAdapter.getCurrentFragment().onBusyFetch();
        }
        mTaskCount = 2;
        new StarNameMyAccountTask(getBaseApplication(), this, mBaseChain, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new StarNameMyDomainTask(getBaseApplication(), this, mBaseChain, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void onTaskResponse(TaskResult result) {
        mTaskCount--;
        if (isFinishing()) return;
        if (result.taskType == TASK_FETCH_MY_STARNAME_ACCOUNT) {
            mMyStarNameAccounts = (ArrayList<StarNameAccount>)result.resultData;

        } else if (result.taskType == TASK_FETCH_MY_STARNAME_DOMAIN) {
            mMyStarNameDomains = (ArrayList<StarNameDomain>)result.resultData;
        }

        if (mTaskCount == 0) {
            onHideWaitDialog();
            mPageAdapter.getCurrentFragment().onRefreshTab();
            WLog.w("mMyStarNameAccounts " + mMyStarNameAccounts.size());
            WLog.w("mMyStarNameDomains " + mMyStarNameDomains.size());
        }
    }

    private class StarNamePageAdapter extends FragmentPagerAdapter {

        private ArrayList<BaseFragment> mFragments = new ArrayList<>();
        private BaseFragment mCurrentFragment;

        public StarNamePageAdapter(FragmentManager fm) {
            super(fm);
            mFragments.clear();
            mFragments.add(MyDomainFragment.newInstance(null));
            mFragments.add(MyAccountFragment.newInstance(null));
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
