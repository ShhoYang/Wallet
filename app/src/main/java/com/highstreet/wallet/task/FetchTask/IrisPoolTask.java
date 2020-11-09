package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResLcdIrisPool;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import retrofit2.Response;

public class IrisPoolTask extends CommonTask {

    public IrisPoolTask(BaseApplication app, TaskListener listener) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_IRIS_POOL;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            Response<ResLcdIrisPool> response = ApiClient.getIrisChain(mApp).getIrisPool().execute();
            if(!response.isSuccessful()) {
                mResult.isSuccess = false;
                mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                return mResult;
            } else {
                mResult.resultData = response.body();
                mResult.isSuccess = true;
            }

        } catch (Exception e) {
            WLog.w("IrisPoolTask Error " + e.getMessage());
        }

        return mResult;
    }
}
