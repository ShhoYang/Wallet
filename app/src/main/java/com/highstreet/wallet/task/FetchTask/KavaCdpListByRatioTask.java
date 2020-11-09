package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResCdpList;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import retrofit2.Response;

public class KavaCdpListByRatioTask extends CommonTask {
    //Maybe do not need app side!!

    private BaseChain mChain;
    private String mDenom;
    private String mRatio;

    public KavaCdpListByRatioTask(BaseApplication app, TaskListener listener, BaseChain chain, String denom, String ratio) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_KAVA_CDP_LIST_RATIO;
        this.mChain = chain;
        this.mRatio = ratio;

    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BaseChain.KAVA_MAIN)) {
                //mainnet not yet!

            } else if (mChain.equals(BaseChain.KAVA_TEST)) {
                Response<ResCdpList> response = ApiClient.getKavaTestChain(mApp).getCdpCoinRate(mDenom, mRatio).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;

                } else {
                    WLog.w("KavaCdpListByRatioTask : NOk");
                }
            }

        } catch (Exception e) {
            WLog.w("KavaCdpListByRatioTask Error " + e.getMessage());
        }
        return mResult;
    }
}