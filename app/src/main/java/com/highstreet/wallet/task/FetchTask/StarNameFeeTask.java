package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResIovFee;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import retrofit2.Response;

public class StarNameFeeTask extends CommonTask {

    private BaseChain mChain;

    public StarNameFeeTask(BaseApplication app, TaskListener listener, BaseChain chain) {
        super(app, listener);
        this.mChain = chain;
        this.mResult.taskType   = BaseConstant.TASK_FETCH_STARNAME_FEE;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BaseChain.IOV_MAIN)) {
                Response<ResIovFee> response = ApiClient.getIovChain(mApp).getFee().execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result.fees;
                    mResult.isSuccess = true;
                }
            } else if (mChain.equals(BaseChain.IOV_TEST)) {
                Response<ResIovFee> response = ApiClient.getIovTestChain(mApp).getFee().execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result.fees;
                    mResult.isSuccess = true;
                }
            }

        } catch (Exception e) {
            WLog.w("StarNameFeeTask Error " + e.getMessage());
        }

        return mResult;
    }
}
