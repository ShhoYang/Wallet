package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResKavaIncentiveParam;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import retrofit2.Response;

public class KavaIncentiveParamTask extends CommonTask {

    private BaseChain mChain;

    public KavaIncentiveParamTask(BaseApplication app, TaskListener listener, BaseChain chain) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_KAVA_INCENTIVE_PARAM;
        this.mChain = chain;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BaseChain.KAVA_MAIN)) {
                Response<ResKavaIncentiveParam> response = ApiClient.getKavaChain(mApp).getIncentiveParams().execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;

                } else {
                    WLog.w("KavaIncentiveParamTask : NOk");
                }

            } else if (mChain.equals(BaseChain.KAVA_TEST)) {
                Response<ResKavaIncentiveParam> response = ApiClient.getKavaTestChain(mApp).getIncentiveParams().execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;

                } else {
                    WLog.w("KavaIncentiveParamTask : NOk");
                }
            }

        } catch (Exception e) {
            WLog.w("KavaIncentiveParamTask Error " + e.getMessage());
        }
        return mResult;
    }
}
