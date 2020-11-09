package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.model.type.Vote;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import java.util.ArrayList;

import retrofit2.Response;

public class IrisVoteListTask extends CommonTask {

    private String proposal_id;


    public IrisVoteListTask(BaseApplication app, TaskListener listener, String id) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_IRIS_VOTE_LIST;
        this.proposal_id = id;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            Response<ArrayList<Vote>> response = ApiClient.getIrisChain(mApp).getVoteList(proposal_id).execute();
            if(response.isSuccessful()) {
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
