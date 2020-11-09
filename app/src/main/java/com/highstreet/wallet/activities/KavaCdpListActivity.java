package com.highstreet.wallet.activities;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.highstreet.wallet.R;
import com.highstreet.wallet.base.BaseActivity;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.base.BaseFragment;
import com.highstreet.wallet.fragment.CdpAllFragment;
import com.highstreet.wallet.fragment.CdpMyFragment;
import com.highstreet.wallet.network.res.ResCdpDepositStatus;
import com.highstreet.wallet.network.res.ResCdpList;
import com.highstreet.wallet.network.res.ResCdpOwnerStatus;
import com.highstreet.wallet.network.res.ResCdpParam;
import com.highstreet.wallet.network.res.ResKavaMarketPrice;
import com.highstreet.wallet.task.FetchTask.KavaCdpByOwnerTask;
import com.highstreet.wallet.task.FetchTask.KavaCdpParamTask;
import com.highstreet.wallet.task.FetchTask.KavaMarketPriceTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WDp;
import com.highstreet.wallet.utils.WLog;

import java.util.ArrayList;
import java.util.HashMap;

import static com.highstreet.wallet.base.BaseConstant.TASK_FETCH_KAVA_CDP_OWENER;
import static com.highstreet.wallet.base.BaseConstant.TASK_FETCH_KAVA_TOKEN_PRICE;

public class KavaCdpListActivity extends BaseActivity implements TaskListener {

    private Toolbar                     mToolbar;
    private RelativeLayout              mContentsLayer;
    private TabLayout                   mCdpTapLayer;
    private ViewPager                   mCdpPager;
    private RelativeLayout              mLoadingLayer;
    private CdpPageAdapter              mPageAdapter;

    public ResCdpParam.Result                           mCdpParam;
    public HashMap<String, ResKavaMarketPrice.Result>   mKavaTokenPrices = new HashMap<>();
    public HashMap<String, ResCdpOwnerStatus.MyCDP>     mMyOwenCdps = new HashMap<>();

    //not need yet!!!
    public HashMap<String, ArrayList<ResCdpDepositStatus.Result>>      mMyDepositedCdp;
    public HashMap<String, ArrayList<ResCdpList.Result>>               mAllCdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kava_cdp_list);
        mToolbar            = findViewById(R.id.tool_bar);
        mContentsLayer      = findViewById(R.id.ContentsLayer);
        mCdpTapLayer        = findViewById(R.id.cdp_tab);
        mCdpPager           = findViewById(R.id.cdp_view_pager);
        mLoadingLayer       = findViewById(R.id.loadingLayer);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAccount = getBaseDao().onSelectAccount(getBaseDao().getLastUser());
        mBaseChain = BaseChain.getChain(mAccount.baseChain);

        mPageAdapter = new CdpPageAdapter(getSupportFragmentManager());
        mCdpPager.setAdapter(mPageAdapter);
        mCdpTapLayer.setupWithViewPager(mCdpPager);
        mCdpTapLayer.setTabRippleColor(null);

        View tab0 = LayoutInflater.from(this).inflate(R.layout.view_tab_myvalidator, null);
        TextView tabItemText0 = tab0.findViewById(R.id.tabItemText);
        tabItemText0.setText(R.string.str_my_cdps);
        tabItemText0.setTextColor(WDp.getTabColor(this, mBaseChain));
        mCdpTapLayer.getTabAt(0).setCustomView(tab0);

        View tab1 = LayoutInflater.from(this).inflate(R.layout.view_tab_myvalidator, null);
        TextView tabItemText1 = tab1.findViewById(R.id.tabItemText);
        tabItemText1.setTextColor(WDp.getTabColor(this, mBaseChain));
        tabItemText1.setText(R.string.str_all_cdps);
        mCdpTapLayer.getTabAt(1).setCustomView(tab1);

        mCdpPager.setOffscreenPageLimit(2);
        mCdpPager.setCurrentItem(0, false);
        mCdpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) { }

            @Override
            public void onPageScrollStateChanged(int i) { }

            @Override
            public void onPageSelected(int i) {
                mPageAdapter.mFragments.get(i).onRefreshTab();
            }
        });
        onFetchCdpInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
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


    private int mTaskCount = 0;
    public void onFetchCdpInfo() {
        if (mBaseChain.equals(BaseChain.KAVA_MAIN) || mBaseChain.equals(BaseChain.KAVA_TEST)) {
            mTaskCount = 1;
            mKavaTokenPrices.clear();
            mMyOwenCdps.clear();
            new KavaCdpParamTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }
    }

    @Override
    public void onTaskResponse(TaskResult result) {
        if(isFinishing()) return;
        mTaskCount--;
        if (result.taskType == BaseConstant.TASK_FETCH_KAVA_CDP_PARAM) {
            if (result.isSuccess && result.resultData != null) {
                final ResCdpParam.Result cdpParam = (ResCdpParam.Result)result.resultData;
                getBaseDao().mKavaCdpParams = cdpParam;
                getBaseDao().mKavaTokenPrices.clear();
                WLog.w("cdpParam.collateral_params.size :  " + cdpParam.collateral_params.size());
                if (cdpParam != null && cdpParam.collateral_params != null && cdpParam.collateral_params.size() > 0) {
                    mTaskCount = mTaskCount + (cdpParam.collateral_params.size() * 2);
                    for (ResCdpParam.KavaCollateralParam param:getBaseDao().mKavaCdpParams.collateral_params) {
                        WLog.w("spot_market_id " + param.spot_market_id);
                        WLog.w("liquidation_market_id " + param.liquidation_market_id);
                        new KavaMarketPriceTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain), param.liquidation_market_id).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        new KavaCdpByOwnerTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain), mAccount.address, param).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            }

        } else if (result.taskType == TASK_FETCH_KAVA_TOKEN_PRICE) {
            if (result.isSuccess && result.resultData != null) {
                final ResKavaMarketPrice.Result price = (ResKavaMarketPrice.Result)result.resultData;
                getBaseDao().mKavaTokenPrices.put(price.market_id, price);
                WLog.w("mKavaTokenPrices put " + price.market_id + " " + price.price);
            }
        } else if (result.taskType == TASK_FETCH_KAVA_CDP_OWENER) {
            if (result.isSuccess && result.resultData != null) {
                final ResCdpOwnerStatus.MyCDP myCdp = (ResCdpOwnerStatus.MyCDP)result.resultData;
                mMyOwenCdps.put(result.resultData2, myCdp);
            }
        }
        if (mTaskCount == 0) {
            mCdpParam = getBaseDao().mKavaCdpParams;
            mKavaTokenPrices = getBaseDao().mKavaTokenPrices;
            if (mCdpParam == null || mKavaTokenPrices == null || mKavaTokenPrices.size() == 0) {
                WLog.e("ERROR No cdp param data");
                onBackPressed();
                return;
            }
            mContentsLayer.setVisibility(View.VISIBLE);
            mLoadingLayer.setVisibility(View.GONE);
            mPageAdapter.mFragments.get(0).onRefreshTab();
            mPageAdapter.mFragments.get(1).onRefreshTab();
        }

    }

    private class CdpPageAdapter extends FragmentPagerAdapter {

        private ArrayList<BaseFragment> mFragments = new ArrayList<>();
        private BaseFragment mCurrentFragment;

        public CdpPageAdapter(FragmentManager fm) {
            super(fm);
            mFragments.clear();
            mFragments.add(CdpMyFragment.newInstance(null));
            mFragments.add(CdpAllFragment.newInstance(null));
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
