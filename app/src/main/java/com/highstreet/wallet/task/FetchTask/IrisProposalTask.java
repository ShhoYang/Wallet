package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.model.type.IrisProposal;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import java.util.ArrayList;

import retrofit2.Response;

public class IrisProposalTask extends CommonTask {

    public IrisProposalTask(BaseApplication app, TaskListener listener) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_IRIS_PROPOSAL;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            Response<ArrayList<IrisProposal>> response = ApiClient.getIrisChain(mApp).getProposalList().execute();
            if(!response.isSuccessful()) {
                mResult.isSuccess = false;
                mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                return mResult;
            }

            if(response.body() != null && response.body().size() > 0) {
                mResult.resultData = response.body();
                mResult.isSuccess = true;
            }


        } catch (Exception e) {
            WLog.w("AllProposalTask Error " + e.getMessage());
        }

        return mResult;
    }
}
