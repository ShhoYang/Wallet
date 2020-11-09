package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResApiTxList;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import java.util.ArrayList;

import retrofit2.Response;

public class ApiStakeTxsHistoryTask extends CommonTask {

    private String mAddress;
    private String mValOpAddress;
    private BaseChain mChain;

    public ApiStakeTxsHistoryTask(BaseApplication app, TaskListener listener, String address, String vAddress, BaseChain chain) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_API_STAKE_HISTORY;
        this.mAddress = address;
        this.mValOpAddress = vAddress;
        this.mChain = chain;
    }


    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BaseChain.COSMOS_MAIN)) {
                Response<ArrayList<ResApiTxList.Data>> response = ApiClient.getCosmosApi(mApp).getStakeTxs(mAddress, mValOpAddress).execute();
                if (response.isSuccessful() && response.body() != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;
                } else {
                    WLog.w("ApiStakeTxsHistoryTask : NOk");
                }
            } else if (mChain.equals(BaseChain.IRIS_MAIN)) {
                Response<ArrayList<ResApiTxList.Data>> response = ApiClient.getIrisApi(mApp).getStakeTxs(mAddress, mValOpAddress).execute();
                if (response.isSuccessful() && response.body() != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;
                } else {
                    WLog.w("ApiStakeTxsHistoryTask : NOk");
                }
            } else if (mChain.equals(BaseChain.BNB_MAIN)) {
            } else if (mChain.equals(BaseChain.KAVA_MAIN)) {
                Response<ArrayList<ResApiTxList.Data>> response = ApiClient.getKavaApi(mApp).getStakeTxs(mAddress, mValOpAddress).execute();
                if (response.isSuccessful() && response.body() != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;
                } else {
                    WLog.w("ApiStakeTxsHistoryTask : NOk");
                }
            } else if (mChain.equals(BaseChain.KAVA_TEST)) {
                Response<ArrayList<ResApiTxList.Data>> response = ApiClient.getKavaTestApi(mApp).getStakeTxs(mAddress, mValOpAddress).execute();
                if (response.isSuccessful() && response.body() != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;
                } else {
                    WLog.w("ApiStakeTxsHistoryTask : NOk");
                }

            } else if (mChain.equals(BaseChain.IOV_MAIN)) {
                Response<ArrayList<ResApiTxList.Data>> response = ApiClient.getIovApi(mApp).getStakeTxs(mAddress, mValOpAddress).execute();
                if (response.isSuccessful() && response.body() != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;
                } else {
                    WLog.w("ApiStakeTxsHistoryTask : NOk");
                }

            } else if (mChain.equals(BaseChain.BAND_MAIN)) {
                Response<ArrayList<ResApiTxList.Data>> response = ApiClient.getBandApi(mApp).getStakeTxs(mAddress, mValOpAddress).execute();
                if (response.isSuccessful() && response.body() != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;
                } else {
                    WLog.w("ApiStakeTxsHistoryTask : NOk");
                }

            } else if (mChain.equals(BaseChain.CERTIK_TEST)) {
                Response<ArrayList<ResApiTxList.Data>> response = ApiClient.getCertikTestApi(mApp).getStakeTxs(mAddress, mValOpAddress).execute();
                if (response.isSuccessful() && response.body() != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;
                } else {
                    WLog.w("ApiStakeTxsHistoryTask : NOk");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            WLog.w("ApiStakeTxsHistoryTask Error " + e.getMessage());
        }
        return mResult;
    }

}
