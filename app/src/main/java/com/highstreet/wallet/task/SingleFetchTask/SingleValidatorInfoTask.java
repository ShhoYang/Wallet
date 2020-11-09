package com.highstreet.wallet.task.SingleFetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.model.type.Validator;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResLcdSingleValidator;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import retrofit2.Response;

import static com.highstreet.wallet.base.BaseChain.BAND_MAIN;
import static com.highstreet.wallet.base.BaseChain.CERTIK_TEST;
import static com.highstreet.wallet.base.BaseChain.COSMOS_MAIN;
import static com.highstreet.wallet.base.BaseChain.IOV_MAIN;
import static com.highstreet.wallet.base.BaseChain.IOV_TEST;
import static com.highstreet.wallet.base.BaseChain.IRIS_MAIN;
import static com.highstreet.wallet.base.BaseChain.KAVA_MAIN;
import static com.highstreet.wallet.base.BaseChain.KAVA_TEST;

public class SingleValidatorInfoTask extends CommonTask {

    private String      mValidatorAddr;
    private BaseChain   mChain;

    public SingleValidatorInfoTask(BaseApplication app, TaskListener listener, String validatorAddr, BaseChain chain) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_SINGLE_VALIDATOR;
        this.mValidatorAddr     = validatorAddr;
        this.mChain = chain;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(COSMOS_MAIN)) {
                Response<ResLcdSingleValidator> response = ApiClient.getCosmosChain(mApp).getValidatorDetail(mValidatorAddr).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(IRIS_MAIN)) {
                Response<Validator> response = ApiClient.getIrisChain(mApp).getValidatorDetail(mValidatorAddr).execute();
                if(response.isSuccessful() && response.body() != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;
                }
            } else if (mChain.equals(KAVA_MAIN)) {
                Response<ResLcdSingleValidator> response = ApiClient.getKavaChain(mApp).getValidatorDetail(mValidatorAddr).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(KAVA_TEST)) {
                Response<ResLcdSingleValidator> response = ApiClient.getKavaTestChain(mApp).getValidatorDetail(mValidatorAddr).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(BAND_MAIN)) {
                Response<ResLcdSingleValidator> response = ApiClient.getBandChain(mApp).getValidatorDetail(mValidatorAddr).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(IOV_MAIN)) {
                Response<ResLcdSingleValidator> response = ApiClient.getIovChain(mApp).getValidatorDetail(mValidatorAddr).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(IOV_TEST)) {
                Response<ResLcdSingleValidator> response = ApiClient.getIovTestChain(mApp).getValidatorDetail(mValidatorAddr).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(CERTIK_TEST)) {
                Response<ResLcdSingleValidator> response = ApiClient.getCertikTestChain(mApp).getValidatorDetail(mValidatorAddr).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            }

        } catch (Exception e) {
            WLog.w("SingleValidatorInfoTask Error " + e.getMessage());
        }
        return mResult;
    }
}
