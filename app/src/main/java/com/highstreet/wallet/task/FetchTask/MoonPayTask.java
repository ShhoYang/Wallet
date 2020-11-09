package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.req.ReqMoonPayKey;
import com.highstreet.wallet.network.res.ResMoonPaySignature;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;

import retrofit2.Response;

public class MoonPayTask extends CommonTask {

    private String mQuery;

    public MoonPayTask(BaseApplication app, TaskListener listener, String query) {
        super(app, listener);
        this.mQuery = query;
        this.mResult.taskType = BaseConstant.TASK_MOON_PAY_SIGNATURE;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            ReqMoonPayKey reqMoonPayKey = new ReqMoonPayKey();
            reqMoonPayKey.api_key = mQuery;
            Response<ResMoonPaySignature> response = ApiClient.getCosmostation(mApp).getMoonPay(reqMoonPayKey).execute();
            if (response.isSuccessful() && response.body() != null && response.body().signature != null) {
                mResult.isSuccess = true;
                mResult.resultData = response.body().signature;
            }

        } catch (Exception e) {
            if(BaseConstant.IS_SHOWLOG) e.printStackTrace();

        }
        return mResult;
    }
}