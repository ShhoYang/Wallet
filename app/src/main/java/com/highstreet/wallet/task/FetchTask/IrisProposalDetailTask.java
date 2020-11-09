package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.model.type.IrisProposal;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import retrofit2.Response;

public class IrisProposalDetailTask extends CommonTask {

    private String proposal_id;


    public IrisProposalDetailTask(BaseApplication app, TaskListener listener, String id) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_IRIS_PROPOSAL_DETAIL;
        this.proposal_id = id;
    }


    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            Response<IrisProposal> response = ApiClient.getIrisChain(mApp).getProposalDetail(proposal_id).execute();
            if (response.isSuccessful()) {
                mResult.isSuccess = true;
                mResult.resultData = response.body();
            }

        } catch (Exception e) {
            WLog.w("AllProposalTask Error " + e.getMessage());
        }

        return mResult;
    }
}