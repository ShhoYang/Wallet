package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResLcdIrisRedelegate;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import java.util.ArrayList;

import retrofit2.Response;

public class IrisRedelegateStateTask extends CommonTask {

    private Account mAccount;

    public IrisRedelegateStateTask(BaseApplication app, TaskListener listener, Account account) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_IRIS_REDELEGATE;
        this.mAccount = account;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            Response<ArrayList<ResLcdIrisRedelegate>> response = ApiClient.getIrisChain(mApp).getRedelegateState(mAccount.address).execute();
            if(response.isSuccessful()) {
                if(response.body() != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;
                } else {
                    mResult.isSuccess = true;
                }
            }

        } catch (Exception e) {
            WLog.w("SingleUnBondingStateTask Error " + e.getMessage());
        }
        return mResult;
    }
}
