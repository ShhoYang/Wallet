package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResOkTokenList;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import retrofit2.Response;

public class OkTokenListTask extends CommonTask {

    private BaseChain mChain;

    public OkTokenListTask(BaseApplication app, TaskListener listener, BaseChain chain) {
        super(app, listener);
        this.mChain             = chain;
        this.mResult.taskType   = BaseConstant.TASK_FETCH_OK_TOKEN_LIST;

    }


    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BaseChain.OK_TEST)) {
                Response<ResOkTokenList> response = ApiClient.getOkTestChain(mApp).getTokenList().execute();
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