package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResLcdIrisReward;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import retrofit2.Response;

public class IrisRewardTask extends CommonTask {

    private Account mAccount;

    public IrisRewardTask(BaseApplication app, TaskListener listener, Account account) {
        super(app, listener);
        this.mAccount           = account;
        this.mResult.taskType   = BaseConstant.TASK_IRIS_REWARD;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            Response<ResLcdIrisReward> response = ApiClient.getIrisChain(mApp).getRewardsInfo(mAccount.address).execute();
            if(!response.isSuccessful()) {
                mResult.isSuccess = false;
                mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                return mResult;
            } else {
                mResult.resultData = response.body();
                mResult.isSuccess = true;
            }

        } catch (Exception e) {
            WLog.w("IrisRewardTask Error " + e.getMessage());
        }

        return mResult;
    }
}
