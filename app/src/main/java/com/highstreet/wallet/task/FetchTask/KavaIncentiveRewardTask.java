package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResKavaIncentiveReward;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import retrofit2.Response;

public class KavaIncentiveRewardTask extends CommonTask {

    private BaseChain mChain;
    private Account mAccount;
    private String mDenom;

    public KavaIncentiveRewardTask(BaseApplication app, TaskListener listener, BaseChain chain, Account account, String denom) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_KAVA_INCENTIVE_REWARD;
        this.mChain = chain;
        this.mAccount = account;
        this.mDenom = denom;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BaseChain.KAVA_MAIN)) {
                Response<ResKavaIncentiveReward> response = ApiClient.getKavaChain(mApp).getIncentive(mAccount.address, mDenom).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(BaseChain.KAVA_TEST)) {
                Response<ResKavaIncentiveReward> response = ApiClient.getKavaTestChain(mApp).getIncentive(mAccount.address, mDenom).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }
            }

        } catch (Exception e) {
            WLog.w("KavaIncentiveRewardTask Error " + e.getMessage());
        }
        return mResult;
    }
}
