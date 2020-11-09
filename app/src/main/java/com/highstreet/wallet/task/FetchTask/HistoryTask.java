package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.req.ReqTx;
import com.highstreet.wallet.network.res.ResBnbHistories;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import retrofit2.Call;
import retrofit2.Response;

public class HistoryTask extends CommonTask {

    private ReqTx   mReq;
    private BaseChain mChain;

    public HistoryTask(BaseApplication app, TaskListener listener, ReqTx mReq, BaseChain chain) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_HISTORY;
        this.mReq = mReq;
        this.mChain = chain;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BaseChain.BNB_MAIN)) {
                Response<ResBnbHistories> response = null;
                if (strings.length == 3) {
                    response = ApiClient.getBnbChain(mApp).getHistory(strings[0], strings[1], strings[2]).execute();
                } else {
                    response = ApiClient.getBnbChain(mApp).getHistoryAsset(strings[0], strings[1], strings[2], strings[3]).execute();
                }

                if(response.isSuccessful() && response.body() != null) {
                    mResult.resultData = response.body().tx;
                    mResult.isSuccess = true;
                } else {
                    WLog.w("HistoryTask : NOk");
                }
                mResult.taskType = BaseConstant.TASK_FETCH_BNB_HISTORY;

            } else if (mChain.equals(BaseChain.BNB_TEST)) {
                Response<ResBnbHistories> response = null;
                if (strings.length == 3) {
                    response = ApiClient.getBnbTestChain(mApp).getHistory(strings[0], strings[1], strings[2]).execute();
                    Call a = ApiClient.getBnbTestChain(mApp).getHistory(strings[0], strings[1], strings[2]);
                    WLog.w("url " + a.request().url().toString());
                } else {
                    response = ApiClient.getBnbTestChain(mApp).getHistoryAsset(strings[0], strings[1], strings[2], strings[3]).execute();
//                    Call a = ApiClient.getBnbTestChain(mApp).getHistoryAsset(strings[0], strings[1], strings[2], strings[3]);
//                    WLog.w("url " + a.request().url().toString());
                }

                if(response.isSuccessful() && response.body() != null) {
                    mResult.resultData = response.body().tx;
                    mResult.isSuccess = true;
                } else {
                    WLog.w("HistoryTask : NOk");
                }
                mResult.taskType = BaseConstant.TASK_FETCH_BNB_HISTORY;

            }

        } catch (Exception e) {
            e.printStackTrace();
            WLog.w("HistoryTask Error " + e.getMessage());
        }
        return mResult;
    }
}
