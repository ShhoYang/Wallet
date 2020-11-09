package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.dao.TotalReward;
import com.highstreet.wallet.model.type.Coin;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Response;

public class TotalRewardTask extends CommonTask {

    private ArrayList<Account> mAccounts;

    public TotalRewardTask(BaseApplication app, TaskListener listener, ArrayList<Account> accounts) {
        super(app, listener);
        this.mAccounts          = accounts;
        this.mResult.taskType   = BaseConstant.TASK_FETCH_TOTAL_REWARDS;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        HashMap<Long, TotalReward>       mTotalRewards = new HashMap<>();
        try {
            for(Account account : mAccounts) {
                Response<ArrayList<Coin>> response = ApiClient.getCosmosChain(mApp).getTotalRewards(account.address).execute();
                if(response.isSuccessful() && response.body() != null && response.body().size() > 0) {
                    TotalReward totalReward = new TotalReward(account.id, response.body());
                    mTotalRewards.put(account.id, totalReward);
                }
            }
            mResult.resultData = mTotalRewards;
            mResult.isSuccess = true;

        } catch (Exception e) {
            WLog.w("TotalRewardTask Error " + e.getMessage());
        }

        return mResult;
    }
}
