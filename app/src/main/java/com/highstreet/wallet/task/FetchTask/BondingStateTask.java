package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResLcdBonding;
import com.highstreet.wallet.network.res.ResLcdBondings;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;
import com.highstreet.wallet.utils.WUtil;

import java.util.ArrayList;

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

public class BondingStateTask extends CommonTask {

    private Account mAccount;

    public BondingStateTask(BaseApplication app, TaskListener listener, Account account) {
        super(app, listener);
        this.mAccount           = account;
        this.mResult.taskType   = BaseConstant.TASK_FETCH_BONDING_STATE;
    }


    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (getChain(mAccount.baseChain).equals(COSMOS_MAIN)) {
                Response<ResLcdBondings> response = ApiClient.getCosmosChain(mApp).getBondingList(mAccount.address).execute();
                if(response.isSuccessful()) {
                    if (response.body() != null && response.body().result != null &&response.body().result.size() > 0) {
                        mApp.getBaseDao().onUpdateBondingStates(mAccount.id, WUtil.getBondingFromLcds(mAccount.id, response.body().result, COSMOS_MAIN));
                    } else {
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                    }
                }

            } else if (getChain(mAccount.baseChain).equals(IRIS_MAIN)) {
                Response<ArrayList<ResLcdBonding>> response = ApiClient.getIrisChain(mApp).getBondingList(mAccount.address).execute();
                if(response.isSuccessful()) {
                    if (response.body() != null && response.body().size() > 0) {
                        mApp.getBaseDao().onUpdateBondingStates(mAccount.id, WUtil.getBondingFromLcds(mAccount.id, response.body(), IRIS_MAIN));
                    } else {
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                    }
                }

            } else if (getChain(mAccount.baseChain).equals(KAVA_MAIN)) {
                Response<ResLcdBondings> response = ApiClient.getKavaChain(mApp).getBondingList(mAccount.address).execute();
                if(response.isSuccessful()) {
                    if (response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                        mApp.getBaseDao().onUpdateBondingStates(mAccount.id, WUtil.getBondingFromLcds(mAccount.id, response.body().result, KAVA_MAIN));
                    } else {
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                    }
                }

            } else if (getChain(mAccount.baseChain).equals(BAND_MAIN)) {
                Response<ResLcdBondings> response = ApiClient.getBandChain(mApp).getBondingList(mAccount.address).execute();
                if(response.isSuccessful()) {
                    if (response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                        mApp.getBaseDao().onUpdateBondingStates(mAccount.id, WUtil.getBondingFromLcds(mAccount.id, response.body().result, BAND_MAIN));
                    } else {
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                    }
                }

            } else if (getChain(mAccount.baseChain).equals(KAVA_TEST)) {
                Response<ResLcdBondings> response = ApiClient.getKavaTestChain(mApp).getBondingList(mAccount.address).execute();
                if(response.isSuccessful()) {
                    if (response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                        mApp.getBaseDao().onUpdateBondingStates(mAccount.id, WUtil.getBondingFromLcds(mAccount.id, response.body().result, KAVA_TEST));
                    } else {
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                    }
                }

            } else if (getChain(mAccount.baseChain).equals(IOV_MAIN)) {
                Response<ResLcdBondings> response = ApiClient.getIovChain(mApp).getBondingList(mAccount.address).execute();
                if(response.isSuccessful()) {
                    if (response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                        mApp.getBaseDao().onUpdateBondingStates(mAccount.id, WUtil.getBondingFromLcds(mAccount.id, response.body().result, IOV_MAIN));
                    } else {
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                    }
                }

            } else if (getChain(mAccount.baseChain).equals(IOV_TEST)) {
                Response<ResLcdBondings> response = ApiClient.getIovTestChain(mApp).getBondingList(mAccount.address).execute();
                if(response.isSuccessful()) {
                    if (response.body() != null && response.body().result != null &&response.body().result.size() > 0) {
                        mApp.getBaseDao().onUpdateBondingStates(mAccount.id, WUtil.getBondingFromLcds(mAccount.id, response.body().result, IOV_TEST));
                    } else {
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                    }
                }

            } else if (getChain(mAccount.baseChain).equals(CERTIK_TEST)) {
                Response<ResLcdBondings> response = ApiClient.getCertikTestChain(mApp).getBondingList(mAccount.address).execute();
                if(response.isSuccessful()) {
                    if (response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                        mApp.getBaseDao().onUpdateBondingStates(mAccount.id, WUtil.getBondingFromLcds(mAccount.id, response.body().result, CERTIK_TEST));
                    } else {
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                    }
                }

            }
            mResult.isSuccess = true;

        } catch (Exception e) {
            WLog.w("BondingStateTask Error " + e.getMessage());
        }
        return mResult;
    }
}
