package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResLcdValidators;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import retrofit2.Response;

public class UnbondedValidatorInfoTask extends CommonTask {
    private BaseChain mChain;

    public UnbondedValidatorInfoTask(BaseApplication app, TaskListener listener, BaseChain chain) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_UNBONDED_VALIDATOR;
        this.mChain = chain;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BaseChain.COSMOS_MAIN)) {
                Response<ResLcdValidators> response = ApiClient.getCosmosChain(mApp).getUnBondedValidatorDetailList().execute();
                if(!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if(response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(BaseChain.KAVA_MAIN)) {
                Response<ResLcdValidators> response = ApiClient.getKavaChain(mApp).getUnBondedValidatorDetailList().execute();
                if(!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if(response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(BaseChain.KAVA_TEST)) {
                Response<ResLcdValidators> response = ApiClient.getKavaTestChain(mApp).getUnBondedValidatorDetailList().execute();
                if(!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if(response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(BaseChain.BAND_MAIN)) {
                Response<ResLcdValidators> response = ApiClient.getBandChain(mApp).getUnBondedValidatorDetailList().execute();
                if(!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if(response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(BaseChain.IOV_MAIN)) {
                Response<ResLcdValidators> response = ApiClient.getIovChain(mApp).getUnBondedValidatorDetailList().execute();
                if(!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if(response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(BaseChain.IOV_TEST)) {
                Response<ResLcdValidators> response = ApiClient.getIovTestChain(mApp).getUnBondedValidatorDetailList().execute();
                if(!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if(response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(BaseChain.CERTIK_TEST)) {
                Response<ResLcdValidators> response = ApiClient.getCertikTestChain(mApp).getUnBondedValidatorDetailList().execute();
                if(!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if(response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            }



        } catch (Exception e) {
            WLog.w("UnbondedValidatorInfo Error " + e.getMessage());
        }

        return mResult;
    }
}
