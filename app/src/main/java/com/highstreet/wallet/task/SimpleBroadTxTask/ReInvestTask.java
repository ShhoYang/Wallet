package com.highstreet.wallet.task.SimpleBroadTxTask;

import com.highstreet.wallet.R;
import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.cosmos.MsgGenerator;
import com.highstreet.wallet.crypto.CryptoHelper;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.dao.Password;
import com.highstreet.wallet.model.type.Coin;
import com.highstreet.wallet.model.type.Fee;
import com.highstreet.wallet.model.type.Msg;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.req.ReqBroadCast;
import com.highstreet.wallet.network.res.ResBroadTx;
import com.highstreet.wallet.network.res.ResLcdAccountInfo;
import com.highstreet.wallet.network.res.ResLcdKavaAccountInfo;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WKey;
import com.highstreet.wallet.utils.WUtil;

import org.bitcoinj.crypto.DeterministicKey;

import java.util.ArrayList;

import retrofit2.Response;

import static com.highstreet.wallet.base.BaseChain.BAND_MAIN;
import static com.highstreet.wallet.base.BaseChain.COSMOS_MAIN;
import static com.highstreet.wallet.base.BaseChain.IOV_MAIN;
import static com.highstreet.wallet.base.BaseChain.IOV_TEST;
import static com.highstreet.wallet.base.BaseChain.IRIS_MAIN;
import static com.highstreet.wallet.base.BaseChain.KAVA_MAIN;
import static com.highstreet.wallet.base.BaseChain.KAVA_TEST;
import static com.highstreet.wallet.base.BaseChain.getChain;
import static com.highstreet.wallet.base.BaseConstant.ERROR_CODE_BROADCAST;

public class ReInvestTask extends CommonTask {

    private Account         mAccount;
    private String          mValidatorAddress;
    private Coin            mReInvestAmount;
    private String          mReInvestMemo;
    private Fee             mReInvestFees;

    public ReInvestTask(BaseApplication app, TaskListener listener, Account mAccount, String mValidatorAddress, Coin mReInvestAmount, String mReInvestMemo, Fee mReInvestFees) {
        super(app, listener);
        this.mAccount = mAccount;
        this.mValidatorAddress = mValidatorAddress;
        this.mReInvestAmount = mReInvestAmount;
        this.mReInvestMemo = mReInvestMemo;
        this.mReInvestFees = mReInvestFees;
        this.mResult.taskType   = BaseConstant.TASK_GEN_TX_REINVEST;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            Password checkPw = mApp.getBaseDao().onSelectPassword();
            if(!CryptoHelper.verifyData(strings[0], checkPw.resource, mApp.getString(R.string.key_password))) {
                mResult.isSuccess = false;
                mResult.errorCode = BaseConstant.ERROR_CODE_INVALID_PASSWORD;
                return mResult;
            }

            if (getChain(mAccount.baseChain).equals(COSMOS_MAIN)) {
                Response<ResLcdAccountInfo> accountResponse = ApiClient.getCosmosChain(mApp).getAccountInfo(mAccount.address).execute();
                if(!accountResponse.isSuccessful()) {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, accountResponse.body()));
                mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, accountResponse.body()));
                mAccount = mApp.getBaseDao().onSelectAccount(""+mAccount.id);

            } else if (getChain(mAccount.baseChain).equals(IRIS_MAIN)) {
                Response<ResLcdAccountInfo> response = ApiClient.getIrisChain(mApp).getBankInfo(mAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, response.body()));
                mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, response.body()));
                mAccount = mApp.getBaseDao().onSelectAccount(""+mAccount.id);

            } else if (getChain(mAccount.baseChain).equals(KAVA_MAIN)) {
                Response<ResLcdKavaAccountInfo> response = ApiClient.getKavaChain(mApp).getAccountInfo(mAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromKavaLcd(mAccount.id, response.body()));
                mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromKavaLcd(mAccount.id, response.body()));
                mAccount = mApp.getBaseDao().onSelectAccount(""+mAccount.id);

            } else if (getChain(mAccount.baseChain).equals(KAVA_TEST)) {
                Response<ResLcdKavaAccountInfo> response = ApiClient.getKavaTestChain(mApp).getAccountInfo(mAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromKavaLcd(mAccount.id, response.body()));
                mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromKavaLcd(mAccount.id, response.body()));
                mAccount = mApp.getBaseDao().onSelectAccount(""+mAccount.id);

            } else if (getChain(mAccount.baseChain).equals(BAND_MAIN)) {
                Response<ResLcdAccountInfo> accountResponse = ApiClient.getBandChain(mApp).getAccountInfo(mAccount.address).execute();
                if(!accountResponse.isSuccessful()) {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, accountResponse.body()));
                mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, accountResponse.body()));
                mAccount = mApp.getBaseDao().onSelectAccount(""+mAccount.id);

            } else if (getChain(mAccount.baseChain).equals(IOV_MAIN)) {
                Response<ResLcdAccountInfo> accountResponse = ApiClient.getIovChain(mApp).getAccountInfo(mAccount.address).execute();
                if(!accountResponse.isSuccessful()) {
                    mResult.errorCode = ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, accountResponse.body()));
                mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, accountResponse.body()));
                mAccount = mApp.getBaseDao().onSelectAccount(""+mAccount.id);

            } else if (getChain(mAccount.baseChain).equals(IOV_TEST)) {
                Response<ResLcdAccountInfo> accountResponse = ApiClient.getIovTestChain(mApp).getAccountInfo(mAccount.address).execute();
                if(!accountResponse.isSuccessful()) {
                    mResult.errorCode = ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, accountResponse.body()));
                mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, accountResponse.body()));
                mAccount = mApp.getBaseDao().onSelectAccount(""+mAccount.id);

            }

            String entropy = CryptoHelper.doDecryptData(mApp.getString(R.string.key_mnemonic) + mAccount.uuid, mAccount.resource, mAccount.spec);
            DeterministicKey deterministicKey = WKey.getKeyWithPathfromEntropy(getChain(mAccount.baseChain), entropy, Integer.parseInt(mAccount.path), mAccount.newBip44);


            Msg withdrawMsg = MsgGenerator.genWithdrawDeleMsg(mAccount.address, mValidatorAddress, getChain(mAccount.baseChain));
            Msg singleDelegateMsg = MsgGenerator.genDelegateMsg(mAccount.address, mValidatorAddress, mReInvestAmount, getChain(mAccount.baseChain));
            ArrayList<Msg> msgs= new ArrayList<>();
            msgs.add(withdrawMsg);
            msgs.add(singleDelegateMsg);

            ReqBroadCast reqBroadCast = MsgGenerator.getBraodcaseReq(mAccount, msgs, mReInvestFees, mReInvestMemo, deterministicKey);
            if (getChain(mAccount.baseChain).equals(COSMOS_MAIN)) {
                Response<ResBroadTx> response = ApiClient.getCosmosChain(mApp).broadTx(reqBroadCast).execute();
                if(response.isSuccessful() && response.body() != null) {
                    if (response.body().txhash != null) {
                        mResult.resultData = response.body().txhash;
                    }
                    if(response.body().code != null) {
                        mResult.errorCode = response.body().code;
                        mResult.errorMsg = response.body().raw_log;
                        return mResult;
                    }
                    mResult.isSuccess = true;

                } else {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                }

            } else if (getChain(mAccount.baseChain).equals(IRIS_MAIN)) {
                Response<ResBroadTx> response = ApiClient.getIrisChain(mApp).broadTx(reqBroadCast).execute();
                if(response.isSuccessful() && response.body() != null) {
                    if (response.body().hash != null) {
                        mResult.resultData = response.body().hash;
                    }
                    if(response.body().check_tx.code != null) {
                        mResult.errorCode = response.body().check_tx.code;
                        mResult.errorMsg = response.body().raw_log;
                        return mResult;
                    }
                    mResult.isSuccess = true;

                } else {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                }

            } else if (getChain(mAccount.baseChain).equals(KAVA_MAIN)) {
                Response<ResBroadTx> response = ApiClient.getKavaChain(mApp).broadTx(reqBroadCast).execute();
                if(response.isSuccessful() && response.body() != null) {
                    if (response.body().txhash != null) {
                        mResult.resultData = response.body().txhash;
                    }
                    if(response.body().code != null) {
                        mResult.errorCode = response.body().code;
                        mResult.errorMsg = response.body().raw_log;
                        return mResult;
                    }
                    mResult.isSuccess = true;

                } else {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                }

            } else if (getChain(mAccount.baseChain).equals(KAVA_TEST)) {
                Response<ResBroadTx> response = ApiClient.getKavaTestChain(mApp).broadTx(reqBroadCast).execute();
                if(response.isSuccessful() && response.body() != null) {
                    if (response.body().txhash != null) {
                        mResult.resultData = response.body().txhash;
                    }
                    if(response.body().code != null) {
                        mResult.errorCode = response.body().code;
                        mResult.errorMsg = response.body().raw_log;
                        return mResult;
                    }
                    mResult.isSuccess = true;

                } else {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                }

            } else if (getChain(mAccount.baseChain).equals(BAND_MAIN)) {
                Response<ResBroadTx> response = ApiClient.getBandChain(mApp).broadTx(reqBroadCast).execute();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().txhash != null) {
                        mResult.resultData = response.body().txhash;
                    }
                    if(response.body().code != null) {
                        mResult.errorCode = response.body().code;
                        mResult.errorMsg = response.body().raw_log;
                        return mResult;
                    }
                    mResult.isSuccess = true;

                } else {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                }

            } else if (getChain(mAccount.baseChain).equals(IOV_MAIN)) {
                Response<ResBroadTx> response = ApiClient.getIovChain(mApp).broadTx(reqBroadCast).execute();
                if(response.isSuccessful() && response.body() != null) {
                    if (response.body().txhash != null) {
                        mResult.resultData = response.body().txhash;
                    }
                    if(response.body().code != null) {
                        mResult.errorCode = response.body().code;
                        mResult.errorMsg = response.body().raw_log;
                        return mResult;
                    }
                    mResult.isSuccess = true;

                } else {
                    mResult.errorCode = ERROR_CODE_BROADCAST;
                }

            } else if (getChain(mAccount.baseChain).equals(IOV_TEST)) {
                Response<ResBroadTx> response = ApiClient.getIovTestChain(mApp).broadTx(reqBroadCast).execute();
                if(response.isSuccessful() && response.body() != null) {
                    if (response.body().txhash != null) {
                        mResult.resultData = response.body().txhash;
                    }
                    if(response.body().code != null) {
                        mResult.errorCode = response.body().code;
                        mResult.errorMsg = response.body().raw_log;
                        return mResult;
                    }
                    mResult.isSuccess = true;

                } else {
                    mResult.errorCode = ERROR_CODE_BROADCAST;
                }

            }


        } catch (Exception e) {
            if(BaseConstant.IS_SHOWLOG) e.printStackTrace();
        }
        return mResult;
    }
}
