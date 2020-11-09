package com.highstreet.wallet.task.SingleFetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.model.type.Redelegate;
import com.highstreet.wallet.model.type.Validator;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResLcdRedelegate;
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

public class SingleRedelegateStateTask extends CommonTask {

    private Account     mAccount;
    private Validator   mToValidtor;

    public SingleRedelegateStateTask(BaseApplication app, TaskListener listener, Account account, Validator toVal) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_SINGLE_REDELEGATE;
        this.mAccount = account;
        this.mToValidtor = toVal;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (getChain(mAccount.baseChain).equals(COSMOS_MAIN)) {
                Response<ResLcdRedelegate> response = ApiClient.getCosmosChain(mApp).getRedelegateHistory(mAccount.address, mToValidtor.operator_address).execute();
                if (response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null) {
                        mResult.resultData = response.body().result;
                        mResult.isSuccess = true;
                    } else {
                        mResult.resultData = new ArrayList<Redelegate>();
                        mResult.isSuccess = true;
                    }
                }

            } else if (getChain(mAccount.baseChain).equals(KAVA_MAIN)) {
                Response<ResLcdRedelegate> response = ApiClient.getKavaChain(mApp).getRedelegateHistory(mAccount.address, mToValidtor.operator_address).execute();
                if (response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null) {
                        mResult.resultData = response.body().result;
                        mResult.isSuccess = true;
                    } else {
                        mResult.resultData = new ArrayList<Redelegate>();
                        mResult.isSuccess = true;
                    }
                }

            } else if (getChain(mAccount.baseChain).equals(KAVA_TEST)) {
                Response<ResLcdRedelegate> response = ApiClient.getKavaTestChain(mApp).getRedelegateHistory(mAccount.address, mToValidtor.operator_address).execute();
                if (response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null) {
                        mResult.resultData = response.body().result;
                        mResult.isSuccess = true;
                    } else {
                        mResult.resultData = new ArrayList<Redelegate>();
                        mResult.isSuccess = true;
                    }
                }

            } else if (getChain(mAccount.baseChain).equals(BAND_MAIN)) {
                Response<ResLcdRedelegate> response = ApiClient.getBandChain(mApp).getRedelegateHistory(mAccount.address, mToValidtor.operator_address).execute();
                if (response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null) {
                        mResult.resultData = response.body().result;
                        mResult.isSuccess = true;
                    } else {
                        mResult.resultData = new ArrayList<Redelegate>();
                        mResult.isSuccess = true;
                    }
                }

            } else if (getChain(mAccount.baseChain).equals(IOV_MAIN)) {
                Response<ResLcdRedelegate> response = ApiClient.getIovChain(mApp).getRedelegateHistory(mAccount.address, mToValidtor.operator_address).execute();
                if (response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null) {
                        mResult.resultData = response.body().result;
                        mResult.isSuccess = true;
                    } else {
                        mResult.resultData = new ArrayList<Redelegate>();
                        mResult.isSuccess = true;
                    }
                }

            } else if (getChain(mAccount.baseChain).equals(IOV_TEST)) {
                Response<ResLcdRedelegate> response = ApiClient.getIovTestChain(mApp).getRedelegateHistory(mAccount.address, mToValidtor.operator_address).execute();
                if (response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null) {
                        mResult.resultData = response.body().result;
                        mResult.isSuccess = true;
                    } else {
                        mResult.resultData = new ArrayList<Redelegate>();
                        mResult.isSuccess = true;
                    }
                }

            } else if (getChain(mAccount.baseChain).equals(CERTIK_TEST)) {
                Response<ResLcdRedelegate> response = ApiClient.getCertikTestChain(mApp).getRedelegateHistory(mAccount.address, mToValidtor.operator_address).execute();
                if (response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null) {
                        mResult.resultData = response.body().result;
                        mResult.isSuccess = true;
                    } else {
                        mResult.resultData = new ArrayList<Redelegate>();
                        mResult.isSuccess = true;
                    }
                }

            }

        } catch (Exception e) {
            WLog.w("SingleUnBondingStateTask Error " + e.getMessage());
        }
        return mResult;
    }

}
