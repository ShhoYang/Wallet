package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.req.ReqStarNameResolve;
import com.highstreet.wallet.network.res.ResIovStarNameResolve;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import retrofit2.Response;

import static com.highstreet.wallet.base.BaseConstant.TASK_FETCH_STARNAME_RESOLVE;

public class StarNameResolveTask extends CommonTask {

    private BaseChain   mBaseChain;
    private String      mStarName;

    public StarNameResolveTask(BaseApplication app, TaskListener listener, BaseChain basecahin, String starname) {
        super(app, listener);
        this.mBaseChain = basecahin;
        this.mStarName = starname;
        this.mResult.taskType = TASK_FETCH_STARNAME_RESOLVE;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        ReqStarNameResolve req = new ReqStarNameResolve(mStarName);
        try {
            if (mBaseChain.equals(BaseChain.IOV_MAIN)) {
                Response<ResIovStarNameResolve> response = ApiClient.getIovChain(mApp).getStarnameAddress(req).execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null && response.body().result.account != null) {
                    mResult.resultData = response.body().result.account;
                    mResult.isSuccess = true;
                }
            } else if (mBaseChain.equals(BaseChain.IOV_TEST)) {
                Response<ResIovStarNameResolve> response = ApiClient.getIovTestChain(mApp).getStarnameAddress(req).execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null && response.body().result.account != null) {
                    mResult.resultData = response.body().result.account;
                    mResult.isSuccess = true;
                }
            }

        } catch (Exception e) {
            WLog.w("StarNameResolveTask Error " + e.getMessage());
        }

        return mResult;
    }
}
