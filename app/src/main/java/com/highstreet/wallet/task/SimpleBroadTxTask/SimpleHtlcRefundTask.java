package com.highstreet.wallet.task.SimpleBroadTxTask;

import com.highstreet.wallet.R;
import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.cosmos.MsgGenerator;
import com.highstreet.wallet.crypto.CryptoHelper;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.model.type.Fee;
import com.highstreet.wallet.model.type.Msg;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.req.ReqBroadCast;
import com.highstreet.wallet.network.res.ResBroadTx;
import com.highstreet.wallet.network.res.ResLcdKavaAccountInfo;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WKey;
import com.highstreet.wallet.utils.WLog;
import com.highstreet.wallet.utils.WUtil;

import org.bitcoinj.crypto.DeterministicKey;

import java.util.ArrayList;

import retrofit2.Response;

import static com.highstreet.wallet.base.BaseConstant.TASK_GEN_TX_HTLC_REFUND;

public class SimpleHtlcRefundTask extends CommonTask {

    private Account         mAccount;
    private String          mSwapId;
    private String          mMemo;
    private Fee             mFees;

    public SimpleHtlcRefundTask(BaseApplication app, TaskListener listener,
                                Account account, String swapid, String memo, Fee fees) {
        super(app, listener);
        this.mAccount = account;
        this.mSwapId = swapid;
        this.mMemo = memo;
        this.mFees = fees;
        this.mResult.taskType = TASK_GEN_TX_HTLC_REFUND;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.KAVA_MAIN)) {
                Response<ResLcdKavaAccountInfo> response = ApiClient.getKavaChain(mApp).getAccountInfo(mAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromKavaLcd(mAccount.id, response.body()));
                mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromKavaLcd(mAccount.id, response.body()));
                mAccount = mApp.getBaseDao().onSelectAccount(""+mAccount.id);

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.KAVA_TEST)) {
                Response<ResLcdKavaAccountInfo> response = ApiClient.getKavaTestChain(mApp).getAccountInfo(mAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromKavaLcd(mAccount.id, response.body()));
                mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromKavaLcd(mAccount.id, response.body()));
                mAccount = mApp.getBaseDao().onSelectAccount(""+mAccount.id);

            }

            String entropy = CryptoHelper.doDecryptData(mApp.getString(R.string.key_mnemonic) + mAccount.uuid, mAccount.resource, mAccount.spec);
            DeterministicKey deterministicKey = WKey.getKeyWithPathfromEntropy(BaseChain.getChain(mAccount.baseChain), entropy, Integer.parseInt(mAccount.path), mAccount.newBip44);

            Msg refundMsg = MsgGenerator.genRefundAtomicSwap(mAccount.address, mSwapId, BaseChain.getChain(mAccount.baseChain));
            ArrayList<Msg> msgs= new ArrayList<>();
            msgs.add(refundMsg);

            WLog.w("refundMsg : " +  WUtil.prettyPrinter(refundMsg));

            ReqBroadCast reqBroadCast = MsgGenerator.getBraodcaseReq(mAccount, msgs, mFees, mMemo, deterministicKey);
            if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.KAVA_MAIN)) {
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

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.KAVA_TEST)) {
                Response<ResBroadTx> response = ApiClient.getKavaTestChain(mApp).broadTx(reqBroadCast).execute();
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
            }


        } catch (Exception e) {
            WLog.w("Exception " + e.getMessage());
        }
        return mResult;
    }
}
