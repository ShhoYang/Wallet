package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResLcdUnBonding;
import com.highstreet.wallet.network.res.ResLcdUnBondings;
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

public class UnBondingStateTask extends CommonTask {

    private Account mAccount;

    public UnBondingStateTask(BaseApplication app, TaskListener listener, Account account) {
        super(app, listener);
        this.mAccount           = account;
        this.mResult.taskType   = BaseConstant.TASK_FETCH_UNBONDING_STATE;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (BaseChain.getChain(mAccount.baseChain).equals(COSMOS_MAIN)) {
                Response<ResLcdUnBondings> response = ApiClient.getCosmosChain(mApp).getUnBondingList(mAccount.address).execute();
                if(response.isSuccessful()) {
                    if (response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                        mApp.getBaseDao().onUpdateUnbondingStates(mAccount.id, WUtil.getUnbondingFromLcds(mApp, COSMOS_MAIN, mAccount.id, response.body().result));
                    } else {
                        mApp.getBaseDao().onDeleteUnbondingStates(mAccount.id);
                    }
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(IRIS_MAIN)) {
                Response<ArrayList<ResLcdUnBonding>> response = ApiClient.getIrisChain(mApp).getUnBondingList(mAccount.address).execute();
                if(response.isSuccessful()) {
                    if (response.body() != null && response.body().size() > 0) {
                        mApp.getBaseDao().onUpdateUnbondingStates(mAccount.id, WUtil.getUnbondingFromLcds(mApp, IRIS_MAIN, mAccount.id, response.body()));
                    } else {
                        mApp.getBaseDao().onDeleteUnbondingStates(mAccount.id);
                    }
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(KAVA_MAIN)) {
                Response<ResLcdUnBondings> response = ApiClient.getKavaChain(mApp).getUnBondingList(mAccount.address).execute();
                if(response.isSuccessful()) {
                    if (response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                        mApp.getBaseDao().onUpdateUnbondingStates(mAccount.id, WUtil.getUnbondingFromLcds(mApp, KAVA_MAIN, mAccount.id, response.body().result));
                    } else {
                        mApp.getBaseDao().onDeleteUnbondingStates(mAccount.id);
                    }
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BAND_MAIN)) {
                Response<ResLcdUnBondings> response = ApiClient.getBandChain(mApp).getUnBondingList(mAccount.address).execute();
                if(response.isSuccessful()) {
                    if (response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                        mApp.getBaseDao().onUpdateUnbondingStates(mAccount.id, WUtil.getUnbondingFromLcds(mApp, BAND_MAIN, mAccount.id, response.body().result));
                    } else {
                        mApp.getBaseDao().onDeleteUnbondingStates(mAccount.id);
                    }
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(KAVA_TEST)) {
                Response<ResLcdUnBondings> response = ApiClient.getKavaTestChain(mApp).getUnBondingList(mAccount.address).execute();
                if(response.isSuccessful()) {
                    if (response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                        mApp.getBaseDao().onUpdateUnbondingStates(mAccount.id, WUtil.getUnbondingFromLcds(mApp, KAVA_TEST, mAccount.id, response.body().result));
                    } else {
                        mApp.getBaseDao().onDeleteUnbondingStates(mAccount.id);
                    }
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(IOV_MAIN)) {
                Response<ResLcdUnBondings> response = ApiClient.getIovChain(mApp).getUnBondingList(mAccount.address).execute();
                if(response.isSuccessful()) {
                    if (response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                        mApp.getBaseDao().onUpdateUnbondingStates(mAccount.id, WUtil.getUnbondingFromLcds(mApp, IOV_MAIN, mAccount.id, response.body().result));
                    } else {
                        mApp.getBaseDao().onDeleteUnbondingStates(mAccount.id);
                    }
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(IOV_TEST)) {
                Response<ResLcdUnBondings> response = ApiClient.getIovTestChain(mApp).getUnBondingList(mAccount.address).execute();
                if(response.isSuccessful()) {
                    if (response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                        mApp.getBaseDao().onUpdateUnbondingStates(mAccount.id, WUtil.getUnbondingFromLcds(mApp, IOV_TEST, mAccount.id, response.body().result));
                    } else {
                        mApp.getBaseDao().onDeleteUnbondingStates(mAccount.id);
                    }
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(CERTIK_TEST)) {
                Response<ResLcdUnBondings> response = ApiClient.getCertikTestChain(mApp).getUnBondingList(mAccount.address).execute();
                if(response.isSuccessful()) {
                    if (response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                        mApp.getBaseDao().onUpdateUnbondingStates(mAccount.id, WUtil.getUnbondingFromLcds(mApp, CERTIK_TEST, mAccount.id, response.body().result));
                    } else {
                        mApp.getBaseDao().onDeleteUnbondingStates(mAccount.id);
                    }
                }

            }
            mResult.isSuccess = true;

        } catch (Exception e) {
            WLog.w("UnBondingStateTask Error " + e.getMessage());
        }
        return mResult;
    }
}
