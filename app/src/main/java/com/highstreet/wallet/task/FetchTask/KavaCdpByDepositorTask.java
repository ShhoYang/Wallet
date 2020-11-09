package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResCdpDepositStatus;
import com.highstreet.wallet.network.res.ResCdpParam;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import retrofit2.Response;

public class KavaCdpByDepositorTask extends CommonTask {

    private BaseChain mChain;
    private String mAddress;

    private ResCdpParam.KavaCollateralParam mParam;

    public KavaCdpByDepositorTask(BaseApplication app, TaskListener listener, BaseChain chain, String address, ResCdpParam.KavaCollateralParam param) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_KAVA_CDP_DEPOSIT;
        this.mChain = chain;
        this.mAddress = address;
        this.mParam = param;

    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BaseChain.KAVA_MAIN)) {
                Response<ResCdpDepositStatus> response = ApiClient.getKavaChain(mApp).getCdpDepositStatus(mAddress, mParam.denom).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;

                } else {
                    WLog.w("KavaCdpByDepositor : NOk");
                }

            } else if (mChain.equals(BaseChain.KAVA_TEST)) {
                Response<ResCdpDepositStatus> response = ApiClient.getKavaTestChain(mApp).getCdpDepositStatus(mAddress, mParam.type).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;

                } else {
                    WLog.w("KavaCdpByDepositor : NOk");
                }
            }

        } catch (Exception e) {
            WLog.w("KavaCdpByDepositor Error " + e.getMessage());
        }
        return mResult;
    }
}