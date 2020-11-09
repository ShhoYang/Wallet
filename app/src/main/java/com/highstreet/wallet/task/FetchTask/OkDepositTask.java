package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResOkDeposit;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import retrofit2.Response;

public class OkDepositTask extends CommonTask {

    private BaseChain mChain;
    private Account mAccount;

    public OkDepositTask(BaseApplication app, TaskListener listener, Account account, BaseChain chain) {
        super(app, listener);
        this.mAccount           = account;
        this.mChain             = chain;
        this.mResult.taskType   = BaseConstant.TASK_FETCH_OK_DEPOSIT;

    }


    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BaseChain.OK_TEST)) {
                Response<ResOkDeposit> response = ApiClient.getOkTestChain(mApp).getDepositInfo(mAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if(response.body() != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;
                }

            }
            mResult.isSuccess = true;

        } catch (Exception e) {
            WLog.w("OkDepositTask Error " + e.getMessage());

        }
        return mResult;
    }

}
