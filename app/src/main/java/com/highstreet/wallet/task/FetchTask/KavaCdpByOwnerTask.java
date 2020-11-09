package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResCdpOwnerStatus;
import com.highstreet.wallet.network.res.ResCdpParam;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import retrofit2.Response;

public class KavaCdpByOwnerTask extends CommonTask {

    private BaseChain mChain;
    private String mAddress;
    private ResCdpParam.KavaCollateralParam mParam;

    public KavaCdpByOwnerTask(BaseApplication app, TaskListener listener, BaseChain chain, String address, ResCdpParam.KavaCollateralParam param) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_KAVA_CDP_OWENER;
        this.mChain = chain;
        this.mAddress = address;
        this.mParam = param;

    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BaseChain.KAVA_MAIN)) {
                Response<ResCdpOwnerStatus> response = ApiClient.getKavaChain(mApp).getCdpStatusByOwner(mAddress, mParam.denom).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.resultData2 = mParam.denom;
                    mResult.isSuccess = true;

                }

            } else if (mChain.equals(BaseChain.KAVA_TEST)) {
                Response<ResCdpOwnerStatus> response = ApiClient.getKavaTestChain(mApp).getCdpStatusByOwner(mAddress, mParam.type).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.resultData2 = mParam.denom;
                    mResult.isSuccess = true;

                }
            }

        } catch (Exception e) {
            WLog.w("KavaCdpByOwnerTask Error " + e.getMessage());
        }
        return mResult;
    }
}