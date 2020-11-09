package com.highstreet.wallet.task.SingleFetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResLcdBonding;
import com.highstreet.wallet.network.res.ResLcdSingleBonding;
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
import static com.highstreet.wallet.base.BaseChain.getChain;

public class SingleBondingStateTask extends CommonTask {

    private Account mAccount;
    private String  mValidatorAddr;

    public SingleBondingStateTask(BaseApplication app, TaskListener listener, Account account, String validatorAddr) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_SINGLE_BONDING;
        this.mAccount = account;
        this.mValidatorAddr = validatorAddr;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (getChain(mAccount.baseChain).equals(COSMOS_MAIN)) {
                Response<ResLcdSingleBonding> response = ApiClient.getCosmosChain(mApp).getBonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null)
                        mApp.getBaseDao().onUpdateBondingState(mAccount.id, WUtil.getBondingFromLcd(mAccount.id, response.body().result, COSMOS_MAIN));
                    else
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                }
                mResult.isSuccess = true;

            } else if (getChain(mAccount.baseChain).equals(IRIS_MAIN)) {
                Response<ResLcdBonding> response = ApiClient.getIrisChain(mApp).getBonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null){
                        mApp.getBaseDao().onUpdateBondingState(mAccount.id, WUtil.getBondingFromLcd(mAccount.id, response.body(), IRIS_MAIN));
                    } else {
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                    }

                }
                mResult.isSuccess = true;

            } else if (getChain(mAccount.baseChain).equals(KAVA_MAIN)) {
                Response<ResLcdSingleBonding> response = ApiClient.getKavaChain(mApp).getBonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null)
                        mApp.getBaseDao().onUpdateBondingState(mAccount.id, WUtil.getBondingFromLcd(mAccount.id, response.body().result, KAVA_MAIN));
                    else
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                }
                mResult.isSuccess = true;

            } else if (getChain(mAccount.baseChain).equals(KAVA_TEST)) {
                Response<ResLcdSingleBonding> response = ApiClient.getKavaTestChain(mApp).getBonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null)
                        mApp.getBaseDao().onUpdateBondingState(mAccount.id, WUtil.getBondingFromLcd(mAccount.id, response.body().result, KAVA_TEST));
                    else
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                }
                mResult.isSuccess = true;

            } else if (getChain(mAccount.baseChain).equals(BAND_MAIN)) {
                Response<ResLcdSingleBonding> response = ApiClient.getBandChain(mApp).getBonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null)
                        mApp.getBaseDao().onUpdateBondingState(mAccount.id, WUtil.getBondingFromLcd(mAccount.id, response.body().result, BAND_MAIN));
                    else
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                }
                mResult.isSuccess = true;

            } else if (getChain(mAccount.baseChain).equals(IOV_MAIN)) {
                Response<ResLcdSingleBonding> response = ApiClient.getIovChain(mApp).getBonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null)
                        mApp.getBaseDao().onUpdateBondingState(mAccount.id, WUtil.getBondingFromLcd(mAccount.id, response.body().result, IOV_MAIN));
                    else
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                }
                mResult.isSuccess = true;

            } else if (getChain(mAccount.baseChain).equals(IOV_TEST)) {
                Response<ResLcdSingleBonding> response = ApiClient.getIovTestChain(mApp).getBonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null)
                        mApp.getBaseDao().onUpdateBondingState(mAccount.id, WUtil.getBondingFromLcd(mAccount.id, response.body().result, IOV_TEST));
                    else
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                }
                mResult.isSuccess = true;

            } else if (getChain(mAccount.baseChain).equals(CERTIK_TEST)) {
                Response<ResLcdSingleBonding> response = ApiClient.getCertikTestChain(mApp).getBonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null)
                        mApp.getBaseDao().onUpdateBondingState(mAccount.id, WUtil.getBondingFromLcd(mAccount.id, response.body().result, CERTIK_TEST));
                    else
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                }
                mResult.isSuccess = true;

            }



        } catch (Exception e) {
            WLog.w("SingleBondingStateTask Error " + e.getMessage());
        }
        return mResult;
    }
}
