package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.dao.BnbToken;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import java.util.ArrayList;

import retrofit2.Response;

public class BnbMiniTokenListTask extends CommonTask {

    private Account mAccount;

    public BnbMiniTokenListTask(BaseApplication app, TaskListener listener, Account account) {
        super(app, listener);
        this.mAccount           = account;
        this.mResult.taskType   = BaseConstant.TASK_FETCH_BNB_MINI_TOKENS;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.BNB_MAIN)) {
                Response<ArrayList<BnbToken>> response = ApiClient.getBnbChain(mApp).getMiniTokens("3000").execute();
                if(response.isSuccessful() && response.body() != null && response.body().size() > 0) {
                    mResult.resultData = response.body();
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.BNB_TEST)) {
                Response<ArrayList<BnbToken>> response = ApiClient.getBnbTestChain(mApp).getMiniTokens("3000").execute();
                if (response.isSuccessful() && response.body() != null && response.body().size() > 0) {
                    mResult.resultData = response.body();
                }
            }
            mResult.isSuccess = true;

        } catch (Exception e) {
            WLog.w("BnbTokenList Error " + e.getMessage());
        }
        return mResult;
    }
}