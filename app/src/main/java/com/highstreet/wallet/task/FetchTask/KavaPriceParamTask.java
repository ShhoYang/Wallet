package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResKavaPriceParam;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import retrofit2.Response;

public class KavaPriceParamTask extends CommonTask {

    private BaseChain mChain;

    public KavaPriceParamTask(BaseApplication app, TaskListener listener, BaseChain chain) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_KAVA_PRICE_PARAM;
        this.mChain = chain;

    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BaseChain.KAVA_MAIN)) {
                Response<ResKavaPriceParam> response = ApiClient.getKavaChain(mApp).getPriceParam().execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;

                } else {
                    WLog.w("KavaPriceParamTask : NOk");
                }

            } else if (mChain.equals(BaseChain.KAVA_TEST)) {
                Response<ResKavaPriceParam> response = ApiClient.getKavaTestChain(mApp).getPriceParam().execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;

                } else {
                    WLog.w("KavaPriceParamTask : NOk");
                }
            }

        } catch (Exception e) {
            WLog.w("KavaPriceParamTask Error " + e.getMessage());
        }
        return mResult;
    }
}