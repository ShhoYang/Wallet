package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResBnbAccountInfo;
import com.highstreet.wallet.network.res.ResLcdAccountInfo;
import com.highstreet.wallet.network.res.ResLcdKavaAccountInfo;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;
import com.highstreet.wallet.utils.WUtil;

import retrofit2.Response;

public class AccountInfoTask extends CommonTask {

    private Account mAccount;

    public AccountInfoTask(BaseApplication app, TaskListener listener, Account account) {
        super(app, listener);
        this.mAccount           = account;
        this.mResult.taskType   = BaseConstant.TASK_FETCH_ACCOUNT;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.COSMOS_MAIN)) {
                Response<ResLcdAccountInfo> response = ApiClient.getCosmosChain(mApp).getAccountInfo(mAccount.address).execute();
                if(response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, response.body()));
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.IRIS_MAIN)) {
                Response<ResLcdAccountInfo> response = ApiClient.getIrisChain(mApp).getBankInfo(mAccount.address).execute();
                if(response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, response.body()));
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.BNB_MAIN)) {
                Response<ResBnbAccountInfo> response = ApiClient.getBnbChain(mApp).getAccountInfo(mAccount.address).execute();
                if(response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromBnbLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromBnbLcd(mAccount.id, response.body()));
                } else {
                    mApp.getBaseDao().onDeleteBalance(""+mAccount.id);
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.KAVA_MAIN)) {
                Response<ResLcdKavaAccountInfo> response = ApiClient.getKavaChain(mApp).getAccountInfo(mAccount.address).execute();
                if(response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromKavaLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromKavaLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().mKavaAccount = response.body().result;
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.BAND_MAIN)) {
                Response<ResLcdAccountInfo> response = ApiClient.getBandChain(mApp).getAccountInfo(mAccount.address).execute();
                if(response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, response.body()));
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.BNB_TEST)) {
                Response<ResBnbAccountInfo> response = ApiClient.getBnbTestChain(mApp).getAccountInfo(mAccount.address).execute();
                if(response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromBnbLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromBnbLcd(mAccount.id, response.body()));
                } else {
                    mApp.getBaseDao().onDeleteBalance(""+mAccount.id);
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.KAVA_TEST)) {
                Response<ResLcdKavaAccountInfo> response = ApiClient.getKavaTestChain(mApp).getAccountInfo(mAccount.address).execute();
                if (response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromKavaLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromKavaLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().mKavaAccount = response.body().result;
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.IOV_MAIN)) {
                Response<ResLcdAccountInfo> response = ApiClient.getIovChain(mApp).getAccountInfo(mAccount.address).execute();
                if(response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, response.body()));
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.IOV_TEST)) {
                Response<ResLcdAccountInfo> response = ApiClient.getIovTestChain(mApp).getAccountInfo(mAccount.address).execute();
                if(response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, response.body()));
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.OK_TEST)) {
                Response<ResLcdAccountInfo> response = ApiClient.getOkTestChain(mApp).getAccountInfo(mAccount.address).execute();
                if(response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, response.body()));
//                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, response.body()));

                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.CERTIK_TEST)) {
                Response<ResLcdAccountInfo> response = ApiClient.getCertikTestChain(mApp).getAccountInfo(mAccount.address).execute();
                if(response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, response.body()));
                }

            }
            mResult.isSuccess = true;

        } catch (Exception e) {
            WLog.w("AccountInfoTask Error " + e.getMessage());
            mApp.getBaseDao().onDeleteBalance(""+mAccount.id);

        }
        return mResult;
    }
}
