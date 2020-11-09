package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResMyVote;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import retrofit2.Response;

public class MyVoteCheckTask extends CommonTask {

    private BaseChain mChain;
    private String mProposalId;
    private String mAddress;

    public MyVoteCheckTask(BaseApplication app, TaskListener listener, String proposalId, String address, BaseChain chain) {
        super(app, listener);
        this.mChain = chain;
        this.mProposalId = proposalId;
        this.mAddress = address;
        this.mResult.taskType   = BaseConstant.TASK_FETCH_MY_VOTE;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BaseChain.COSMOS_MAIN)) {
                Response<ResMyVote> response = ApiClient.getCosmosChain(mApp).getMyVote(mProposalId, mAddress).execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }
            } else if (mChain.equals(BaseChain.KAVA_MAIN)) {
                Response<ResMyVote> response = ApiClient.getKavaChain(mApp).getMyVote(mProposalId, mAddress).execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }
            }

        } catch (Exception e) {
            WLog.w("ProposalTallyTask Error " + e.getMessage());
        }

        return mResult;
    }

}
