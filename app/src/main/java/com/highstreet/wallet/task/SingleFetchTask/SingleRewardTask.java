package com.highstreet.wallet.task.SingleFetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.dao.Reward;
import com.highstreet.wallet.model.type.Coin;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResLcdRewardFromVal;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import java.util.ArrayList;

import retrofit2.Response;

import static com.highstreet.wallet.base.BaseChain.BAND_MAIN;
import static com.highstreet.wallet.base.BaseChain.CERTIK_TEST;
import static com.highstreet.wallet.base.BaseChain.COSMOS_MAIN;
import static com.highstreet.wallet.base.BaseChain.IOV_MAIN;
import static com.highstreet.wallet.base.BaseChain.IOV_TEST;
import static com.highstreet.wallet.base.BaseChain.KAVA_MAIN;
import static com.highstreet.wallet.base.BaseChain.KAVA_TEST;
import static com.highstreet.wallet.base.BaseChain.getChain;
import static com.highstreet.wallet.base.BaseConstant.TASK_FETCH_SINGLE_REWARD;

public class SingleRewardTask extends CommonTask {

    private Account mAccount;
    private String  mValidatorAddr;

    public SingleRewardTask(BaseApplication app, TaskListener listener, Account account, String validatorAddr) {
        super(app, listener);
        this.mResult.taskType = TASK_FETCH_SINGLE_REWARD;
        this.mAccount = account;
        this.mValidatorAddr = validatorAddr;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (getChain(mAccount.baseChain).equals(COSMOS_MAIN)) {
                Response<ResLcdRewardFromVal> response = ApiClient.getCosmosChain(mApp).getRewardFromValidator(mAccount.address, mValidatorAddr).execute();
                if(!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }
                if(response.body() != null && response.body().result != null &&response.body().result.size() > 0) {
                    ArrayList<Coin> amounts = response.body().result;
                    long time = System.currentTimeMillis();
                    Reward temp = new Reward(mAccount.id, mValidatorAddr, amounts, time);
                    mResult.resultData = temp;
                    mResult.isSuccess = true;
                }

            } else if (getChain(mAccount.baseChain).equals(KAVA_MAIN)) {
                Response<ResLcdRewardFromVal> response = ApiClient.getKavaChain(mApp).getRewardFromValidator(mAccount.address, mValidatorAddr).execute();
                if(!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }
                if(response.body() != null && response.body().result != null &&response.body().result.size() > 0) {
                    ArrayList<Coin> amounts = response.body().result;
                    long time = System.currentTimeMillis();
                    Reward temp = new Reward(mAccount.id, mValidatorAddr, amounts, time);
                    mResult.resultData = temp;
                    mResult.isSuccess = true;
                }

            } else if (getChain(mAccount.baseChain).equals(BAND_MAIN)) {
                Response<ResLcdRewardFromVal> response = ApiClient.getBandChain(mApp).getRewardFromValidator(mAccount.address, mValidatorAddr).execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }
                if (response.body() != null && response.body().result != null &&response.body().result.size() > 0) {
                    ArrayList<Coin> amounts = response.body().result;
                    long time = System.currentTimeMillis();
                    Reward temp = new Reward(mAccount.id, mValidatorAddr, amounts, time);
                    mResult.resultData = temp;
                    mResult.isSuccess = true;
                }

            } else if (getChain(mAccount.baseChain).equals(KAVA_TEST)) {
                Response<ResLcdRewardFromVal> response = ApiClient.getKavaTestChain(mApp).getRewardFromValidator(mAccount.address, mValidatorAddr).execute();
                if(!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }
                if(response.body() != null && response.body().result != null &&response.body().result.size() > 0) {
                    ArrayList<Coin> amounts = response.body().result;
                    long time = System.currentTimeMillis();
                    Reward temp = new Reward(mAccount.id, mValidatorAddr, amounts, time);
                    mResult.resultData = temp;
                    mResult.isSuccess = true;
                }

            } else if (getChain(mAccount.baseChain).equals(IOV_MAIN)) {
                Response<ResLcdRewardFromVal> response = ApiClient.getIovChain(mApp).getRewardFromValidator(mAccount.address, mValidatorAddr).execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }
                if (response.body() != null && response.body().result != null &&response.body().result.size() > 0) {
                    ArrayList<Coin> amounts = response.body().result;
                    long time = System.currentTimeMillis();
                    Reward temp = new Reward(mAccount.id, mValidatorAddr, amounts, time);
                    mResult.resultData = temp;
                    mResult.isSuccess = true;
                }

            } else if (getChain(mAccount.baseChain).equals(IOV_TEST)) {
                Response<ResLcdRewardFromVal> response = ApiClient.getIovTestChain(mApp).getRewardFromValidator(mAccount.address, mValidatorAddr).execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }
                if (response.body() != null && response.body().result != null &&response.body().result.size() > 0) {
                    ArrayList<Coin> amounts = response.body().result;
                    long time = System.currentTimeMillis();
                    Reward temp = new Reward(mAccount.id, mValidatorAddr, amounts, time);
                    mResult.resultData = temp;
                    mResult.isSuccess = true;
                }

            } else if (getChain(mAccount.baseChain).equals(CERTIK_TEST)) {
                Response<ResLcdRewardFromVal> response = ApiClient.getCertikTestChain(mApp).getRewardFromValidator(mAccount.address, mValidatorAddr).execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }
                if (response.body() != null && response.body().result != null &&response.body().result.size() > 0) {
                    ArrayList<Coin> amounts = response.body().result;
                    long time = System.currentTimeMillis();
                    Reward temp = new Reward(mAccount.id, mValidatorAddr, amounts, time);
                    mResult.resultData = temp;
                    mResult.isSuccess = true;
                }

            }

        } catch (Exception e) {
            WLog.w("SingleRewardTask Error " + e.getMessage());
        }

        return mResult;
    }
}