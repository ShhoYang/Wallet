package com.highstreet.wallet.task.SingleFetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.model.type.Redelegate;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResLcdRedelegate;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import java.util.ArrayList;

import retrofit2.Response;

import static com.highstreet.wallet.base.BaseChain.getChain;

public class SingleAllRedelegateState extends CommonTask {

    private Account mAccount;
    private String  mFromAddress;
    private String  mToAddress;

    public SingleAllRedelegateState(BaseApplication app, TaskListener listener, Account mAccount, String mFromAddress, String mToAddress) {
        super(app, listener);
        this.mAccount = mAccount;
        this.mFromAddress = mFromAddress;
        this.mToAddress = mToAddress;
        this.mResult.taskType   = BaseConstant.TASK_FETCH_SINGLE_ALL_REDELEGATE;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (getChain(mAccount.baseChain).equals(BaseChain.COSMOS_MAIN)) {
                Response<ResLcdRedelegate> response = ApiClient.getCosmosChain(mApp).getRedelegateAllHistory(mAccount.address, mFromAddress, mToAddress).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null) {
                        mResult.resultData = response.body().result;
                        mResult.isSuccess = true;
                    } else {
                        mResult.resultData = new ArrayList<Redelegate>();
                        mResult.isSuccess = true;
                    }
                }

            } else if (getChain(mAccount.baseChain).equals(BaseChain.KAVA_MAIN)) {
                Response<ResLcdRedelegate> response = ApiClient.getKavaChain(mApp).getRedelegateAllHistory(mAccount.address, mFromAddress, mToAddress).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null) {
                        mResult.resultData = response.body().result;
                        mResult.isSuccess = true;
                    } else {
                        mResult.resultData = new ArrayList<Redelegate>();
                        mResult.isSuccess = true;
                    }
                }

            } else if (getChain(mAccount.baseChain).equals(BaseChain.KAVA_TEST)) {
                Response<ResLcdRedelegate> response = ApiClient.getKavaTestChain(mApp).getRedelegateAllHistory(mAccount.address, mFromAddress, mToAddress).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null) {
                        mResult.resultData = response.body().result;
                        mResult.isSuccess = true;
                    } else {
                        mResult.resultData = new ArrayList<Redelegate>();
                        mResult.isSuccess = true;
                    }
                }

            } else if (getChain(mAccount.baseChain).equals(BaseChain.BAND_MAIN)) {
                Response<ResLcdRedelegate> response = ApiClient.getBandChain(mApp).getRedelegateAllHistory(mAccount.address, mFromAddress, mToAddress).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null) {
                        mResult.resultData = response.body().result;
                        mResult.isSuccess = true;
                    } else {
                        mResult.resultData = new ArrayList<Redelegate>();
                        mResult.isSuccess = true;
                    }
                }

            } else if (getChain(mAccount.baseChain).equals(BaseChain.IOV_MAIN)) {
                Response<ResLcdRedelegate> response = ApiClient.getIovChain(mApp).getRedelegateAllHistory(mAccount.address, mFromAddress, mToAddress).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null) {
                        mResult.resultData = response.body().result;
                        mResult.isSuccess = true;
                    } else {
                        mResult.resultData = new ArrayList<Redelegate>();
                        mResult.isSuccess = true;
                    }
                }

            } else if (getChain(mAccount.baseChain).equals(BaseChain.IOV_TEST)) {
                Response<ResLcdRedelegate> response = ApiClient.getIovTestChain(mApp).getRedelegateAllHistory(mAccount.address, mFromAddress, mToAddress).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null) {
                        mResult.resultData = response.body().result;
                        mResult.isSuccess = true;
                    } else {
                        mResult.resultData = new ArrayList<Redelegate>();
                        mResult.isSuccess = true;
                    }
                }

            } else if (getChain(mAccount.baseChain).equals(BaseChain.CERTIK_TEST)) {
                Response<ResLcdRedelegate> response = ApiClient.getCertikTestChain(mApp).getRedelegateAllHistory(mAccount.address, mFromAddress, mToAddress).execute();
                if(response.isSuccessful()) {
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
            WLog.w("SingleAllRedelegateState Error " + e.getMessage());
            if(BaseConstant.IS_SHOWLOG) e.printStackTrace();
        }
        return mResult;
    }

}