package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.req.ReqStarNameDomainInfo;
import com.highstreet.wallet.network.res.ResIovStarNameDomainInfo;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import retrofit2.Response;

import static com.highstreet.wallet.base.BaseConstant.TASK_FETCH_STARNAME_DOMAIN_INFO;

public class StarNameDomainInfoTask extends CommonTask {

    private BaseChain   mBaseChain;
    private String      mDomainName;

    public StarNameDomainInfoTask(BaseApplication app, TaskListener listener, BaseChain basecahin, String domainName) {
        super(app, listener);
        this.mBaseChain = basecahin;
        this.mDomainName = domainName;
        this.mResult.taskType = TASK_FETCH_STARNAME_DOMAIN_INFO;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        ReqStarNameDomainInfo req = new ReqStarNameDomainInfo(mDomainName);
        try {
            if (mBaseChain.equals(BaseChain.IOV_MAIN)) {
                Response<ResIovStarNameDomainInfo> response = ApiClient.getIovChain(mApp).getStarnameDomainInfo(req).execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null && response.body().result.domain != null) {
                    mResult.resultData = response.body().result.domain;
                    mResult.isSuccess = true;
                }
            } else if (mBaseChain.equals(BaseChain.IOV_TEST)) {
                Response<ResIovStarNameDomainInfo> response = ApiClient.getIovTestChain(mApp).getStarnameDomainInfo(req).execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null && response.body().result.domain != null) {
                    mResult.resultData = response.body().result.domain;
                    mResult.isSuccess = true;
                }
            }

        } catch (Exception e) {
            WLog.w("StarNameDomainInfoTask Error " + e.getMessage());
        }

        return mResult;
    }
}
