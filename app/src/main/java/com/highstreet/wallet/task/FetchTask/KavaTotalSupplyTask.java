package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResKavaSupply;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import retrofit2.Response;

public class KavaTotalSupplyTask extends CommonTask {

    private BaseChain mChain;

    public KavaTotalSupplyTask(BaseApplication app, TaskListener listener, BaseChain chain) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_KAVA_TOTAL_SUPPLY;
        this.mChain = chain;

    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BaseChain.KAVA_MAIN)) {
                Response<ResKavaSupply> response = ApiClient.getKavaChain(mApp).getSupply().execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;

                } else {
                    WLog.w("KavaTotalSupplyTask : NOk");
                }

            } else if (mChain.equals(BaseChain.KAVA_TEST)) {
                Response<ResKavaSupply> response = ApiClient.getKavaTestChain(mApp).getSupply().execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;

                } else {
                    WLog.w("KavaTotalSupplyTask : NOk");
                }
            }

        } catch (Exception e) {
            WLog.w("KavaTotalSupplyTask Error " + e.getMessage());
        }
        return mResult;
    }
}