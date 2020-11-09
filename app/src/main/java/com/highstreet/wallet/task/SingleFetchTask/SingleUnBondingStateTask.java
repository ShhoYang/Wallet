package com.highstreet.wallet.task.SingleFetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResLcdSingleUnBonding;
import com.highstreet.wallet.network.res.ResLcdUnBonding;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;
import com.highstreet.wallet.utils.WUtil;

import retrofit2.Response;

import static com.highstreet.wallet.base.BaseChain.BAND_MAIN;
import static com.highstreet.wallet.base.BaseChain.CERTIK_TEST;
import static com.highstreet.wallet.base.BaseChain.COSMOS_MAIN;
import static com.highstreet.wallet.base.BaseChain.IOV_MAIN;
import static com.highstreet.wallet.base.BaseChain.IOV_TEST;
import static com.highstreet.wallet.base.BaseChain.IRIS_MAIN;
import static com.highstreet.wallet.base.BaseChain.KAVA_MAIN;
import static com.highstreet.wallet.base.BaseChain.KAVA_TEST;

public class SingleUnBondingStateTask extends CommonTask {

    private Account mAccount;
    private String  mValidatorAddr;

    public SingleUnBondingStateTask(BaseApplication app, TaskListener listener, Account account, String validatorAddr) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_SINGLE_UNBONDING;
        this.mAccount = account;
        this.mValidatorAddr = validatorAddr;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (BaseChain.getChain(mAccount.baseChain).equals(COSMOS_MAIN)) {
                Response<ResLcdSingleUnBonding> response = ApiClient.getCosmosChain(mApp).getUnbonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mApp.getBaseDao().onUpdateUnbondingStates(mAccount.id, WUtil.getUnbondingFromLcd(mApp, mAccount.id, response.body().result));
                    mResult.isSuccess = true;
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(IRIS_MAIN)) {
                Response<ResLcdUnBonding> response = ApiClient.getIrisChain(mApp).getUnbonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful() && response.body() != null) {
                    mApp.getBaseDao().onUpdateUnbondingStates(mAccount.id, WUtil.getUnbondingFromLcd(mApp, mAccount.id, response.body()));
                    mResult.isSuccess = true;
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(KAVA_MAIN)) {
                Response<ResLcdSingleUnBonding> response = ApiClient.getKavaChain(mApp).getUnbonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mApp.getBaseDao().onUpdateUnbondingStates(mAccount.id, WUtil.getUnbondingFromLcd(mApp, mAccount.id, response.body().result));
                    mResult.isSuccess = true;
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(KAVA_TEST)) {
                Response<ResLcdSingleUnBonding> response = ApiClient.getKavaTestChain(mApp).getUnbonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mApp.getBaseDao().onUpdateUnbondingStates(mAccount.id, WUtil.getUnbondingFromLcd(mApp, mAccount.id, response.body().result));
                    mResult.isSuccess = true;
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BAND_MAIN)) {
                Response<ResLcdSingleUnBonding> response = ApiClient.getBandChain(mApp).getUnbonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mApp.getBaseDao().onUpdateUnbondingStates(mAccount.id, WUtil.getUnbondingFromLcd(mApp, mAccount.id, response.body().result));
                    mResult.isSuccess = true;
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(IOV_MAIN)) {
                Response<ResLcdSingleUnBonding> response = ApiClient.getIovChain(mApp).getUnbonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mApp.getBaseDao().onUpdateUnbondingStates(mAccount.id, WUtil.getUnbondingFromLcd(mApp, mAccount.id, response.body().result));
                    mResult.isSuccess = true;
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(IOV_TEST)) {
                Response<ResLcdSingleUnBonding> response = ApiClient.getIovTestChain(mApp).getUnbonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mApp.getBaseDao().onUpdateUnbondingStates(mAccount.id, WUtil.getUnbondingFromLcd(mApp, mAccount.id, response.body().result));
                    mResult.isSuccess = true;
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(CERTIK_TEST)) {
                Response<ResLcdSingleUnBonding> response = ApiClient.getCertikTestChain(mApp).getUnbonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mApp.getBaseDao().onUpdateUnbondingStates(mAccount.id, WUtil.getUnbondingFromLcd(mApp, mAccount.id, response.body().result));
                    mResult.isSuccess = true;
                }

            }


        } catch (Exception e) {
            WLog.w("SingleUnBondingStateTask Error " + e.getMessage());
        }
        return mResult;
    }
}
