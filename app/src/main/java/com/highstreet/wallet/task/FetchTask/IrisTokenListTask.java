package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.dao.IrisToken;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import java.util.ArrayList;

import retrofit2.Response;

public class IrisTokenListTask extends CommonTask {

    public IrisTokenListTask(BaseApplication app, TaskListener listener) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_IRIS_TOKENS;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            Response<ArrayList<IrisToken>> response = ApiClient.getIrisChain(mApp).getTokens().execute();
            if(response.isSuccessful() && response.body() != null && response.body().size() > 0) {
                mResult.resultData = response.body();
                mResult.isSuccess = true;

            } else {
                WLog.w("IrisTokenList : NOk");
            }

        } catch (Exception e) {
            WLog.w("IrisTokenList Error " + e.getMessage());
        }
        return mResult;
    }
}
