package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResBnbFee;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import java.util.ArrayList;

import retrofit2.Response;

public class BnbFeesTask extends CommonTask {

    private BaseChain mBaseChain;

    public BnbFeesTask(BaseApplication app, TaskListener listener, BaseChain chain) {
        super(app, listener);
        this.mBaseChain = chain;
        this.mResult.taskType   = BaseConstant.TASK_FETCH_BNB_FEES;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mBaseChain.equals(BaseChain.BNB_MAIN)) {
                Response<ArrayList<ResBnbFee>> response = ApiClient.getBnbChain(mApp).getFees().execute();
                if(response.isSuccessful() && response.body() != null && response.body().size() > 0) {
                    mResult.resultData = response.body();
                }

            } else if (mBaseChain.equals(BaseChain.BNB_TEST)) {
                Response<ArrayList<ResBnbFee>> response = ApiClient.getBnbTestChain(mApp).getFees().execute();
                if(response.isSuccessful() && response.body() != null && response.body().size() > 0) {
                    mResult.resultData = response.body();

                }
            }
            mResult.isSuccess = true;

        } catch (Exception e) {
            WLog.w("BnbFeesTask Error " + e.getMessage());
        }
        return mResult;
    }
}