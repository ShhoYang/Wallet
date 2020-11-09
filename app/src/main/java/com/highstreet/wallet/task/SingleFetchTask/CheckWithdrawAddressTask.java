package com.highstreet.wallet.task.SingleFetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResLcdWithDrawAddress;
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
import static com.highstreet.wallet.base.BaseChain.getChain;
import static com.highstreet.wallet.base.BaseConstant.ERROR_CODE_NETWORK;
import static com.highstreet.wallet.base.BaseConstant.TASK_FETCH_WITHDRAW_ADDRESS;

public class CheckWithdrawAddressTask extends CommonTask {

    private Account mAccount;

    public CheckWithdrawAddressTask(BaseApplication app, TaskListener listener, Account account) {
        super(app, listener);
        this.mResult.taskType = TASK_FETCH_WITHDRAW_ADDRESS;
        this.mAccount = account;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (getChain(mAccount.baseChain).equals(COSMOS_MAIN)) {
                Response<ResLcdWithDrawAddress> response = ApiClient.getCosmosChain(mApp).getWithdrawAddress(mAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = ERROR_CODE_NETWORK;
                    return mResult;
                }
                if(response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (getChain(mAccount.baseChain).equals(IRIS_MAIN)) {
                Response<String> response = ApiClient.getIrisChain(mApp).getWithdrawAddress(mAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = ERROR_CODE_NETWORK;
                    return mResult;
                }
                if(response.body() != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;
                }

            } else if (getChain(mAccount.baseChain).equals(KAVA_MAIN)) {
                Response<ResLcdWithDrawAddress> response = ApiClient.getKavaChain(mApp).getWithdrawAddress(mAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = ERROR_CODE_NETWORK;
                    return mResult;
                }
                if(response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (getChain(mAccount.baseChain).equals(KAVA_TEST)) {
                Response<ResLcdWithDrawAddress> response = ApiClient.getKavaTestChain(mApp).getWithdrawAddress(mAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = ERROR_CODE_NETWORK;
                    return mResult;
                }
                if(response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (getChain(mAccount.baseChain).equals(BAND_MAIN)) {
                Response<ResLcdWithDrawAddress> response = ApiClient.getBandChain(mApp).getWithdrawAddress(mAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = ERROR_CODE_NETWORK;
                    return mResult;
                }
                if(response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (getChain(mAccount.baseChain).equals(IOV_MAIN)) {
                Response<ResLcdWithDrawAddress> response = ApiClient.getIovChain(mApp).getWithdrawAddress(mAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = ERROR_CODE_NETWORK;
                    return mResult;
                }
                if(response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (getChain(mAccount.baseChain).equals(IOV_TEST)) {
                Response<ResLcdWithDrawAddress> response = ApiClient.getIovTestChain(mApp).getWithdrawAddress(mAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = ERROR_CODE_NETWORK;
                    return mResult;
                }
                if(response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (getChain(mAccount.baseChain).equals(CERTIK_TEST)) {
                Response<ResLcdWithDrawAddress> response = ApiClient.getCertikTestChain(mApp).getWithdrawAddress(mAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = ERROR_CODE_NETWORK;
                    return mResult;
                }
                if(response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            }

        } catch (Exception e) {
            WLog.w("CheckWithdrawAddressTask Error " + e.getMessage());
        }

        return mResult;
    }
}
