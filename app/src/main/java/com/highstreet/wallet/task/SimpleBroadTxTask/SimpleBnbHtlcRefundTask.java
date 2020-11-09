package com.highstreet.wallet.task.SimpleBroadTxTask;

import com.binance.dex.api.client.BinanceDexApiClientFactory;
import com.binance.dex.api.client.BinanceDexApiRestClient;
import com.binance.dex.api.client.BinanceDexEnvironment;
import com.binance.dex.api.client.Wallet;
import com.binance.dex.api.client.domain.TransactionMetadata;
import com.binance.dex.api.client.domain.broadcast.TransactionOption;
import com.highstreet.wallet.R;
import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.crypto.CryptoHelper;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.dao.Password;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResBnbAccountInfo;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WKey;
import com.highstreet.wallet.utils.WLog;
import com.highstreet.wallet.utils.WUtil;

import org.bitcoinj.crypto.DeterministicKey;

import java.util.List;

import retrofit2.Response;

import static com.highstreet.wallet.base.BaseConstant.TASK_GEN_TX_BNB_HTLC_REFUND;

public class SimpleBnbHtlcRefundTask extends CommonTask {

    private Account         mAccount;
    private String          mSwapId;
    private String          mMemo;

    public SimpleBnbHtlcRefundTask(BaseApplication app, TaskListener listener,
                                Account account, String swapid, String memo) {
        super(app, listener);
        this.mAccount = account;
        this.mSwapId = swapid;
        this.mMemo = memo;
        this.mResult.taskType = TASK_GEN_TX_BNB_HTLC_REFUND;
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

            if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.BNB_MAIN)) {
                Response<ResBnbAccountInfo> response = ApiClient.getBnbChain(mApp).getAccountInfo(mAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromBnbLcd(mAccount.id, response.body()));
                mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromBnbLcd(mAccount.id, response.body()));
                mAccount = mApp.getBaseDao().onSelectAccount(""+mAccount.id);

                String entropy = CryptoHelper.doDecryptData(mApp.getString(R.string.key_mnemonic) + mAccount.uuid, mAccount.resource, mAccount.spec);
                DeterministicKey deterministicKey = WKey.getKeyWithPathfromEntropy(BaseChain.getChain(mAccount.baseChain), entropy, Integer.parseInt(mAccount.path), mAccount.newBip44);

                Wallet wallet = new Wallet(deterministicKey.getPrivateKeyAsHex(), BinanceDexEnvironment.PROD);
                wallet.setAccountNumber(mAccount.accountNumber);
                wallet.setSequence(Long.valueOf(mAccount.sequenceNumber));

                BinanceDexApiRestClient client = BinanceDexApiClientFactory.newInstance().newRestClient(BinanceDexEnvironment.PROD.getBaseUrl());
                TransactionOption options = new TransactionOption(mMemo, 82, null);
                List<TransactionMetadata> resp = client.refundHtlt(mSwapId, wallet, options, true);
                if (resp.get(0).isOk()) {
                    WLog.w("OK " + resp.get(0).getHash());
                    mResult.resultData = resp.get(0).getHash();
                    mResult.isSuccess = true;
                } else {
                    WLog.w("ERROR " + resp.get(0).getCode() + " " + resp.get(0).getLog());
                    mResult.errorCode = resp.get(0).getCode();
                    mResult.errorMsg = resp.get(0).getLog();
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.BNB_TEST)) {
                Response<ResBnbAccountInfo> response = ApiClient.getBnbTestChain(mApp).getAccountInfo(mAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromBnbLcd(mAccount.id, response.body()));
                mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromBnbLcd(mAccount.id, response.body()));
                mAccount = mApp.getBaseDao().onSelectAccount(""+mAccount.id);

                String entropy = CryptoHelper.doDecryptData(mApp.getString(R.string.key_mnemonic) + mAccount.uuid, mAccount.resource, mAccount.spec);
                DeterministicKey deterministicKey = WKey.getKeyWithPathfromEntropy(BaseChain.getChain(mAccount.baseChain), entropy, Integer.parseInt(mAccount.path), mAccount.newBip44);

                Wallet wallet = new Wallet(deterministicKey.getPrivateKeyAsHex(), BinanceDexEnvironment.TEST_NET);
                wallet.setAccountNumber(mAccount.accountNumber);
                wallet.setSequence(Long.valueOf(mAccount.sequenceNumber));

                BinanceDexApiRestClient client = BinanceDexApiClientFactory.newInstance().newRestClient(BinanceDexEnvironment.TEST_NET.getBaseUrl());
                TransactionOption options = new TransactionOption(mMemo, 82, null);
                List<TransactionMetadata> resp = client.refundHtlt(mSwapId, wallet, options, true);
                if (resp.get(0).isOk()) {
                    WLog.w("OK " + resp.get(0).getHash());
                    mResult.resultData = resp.get(0).getHash();
                    mResult.isSuccess = true;
                } else {
                    WLog.w("ERROR " + resp.get(0).getCode() + " " + resp.get(0).getLog());
                    mResult.errorCode = resp.get(0).getCode();
                    mResult.errorMsg = resp.get(0).getLog();
                }
            }


        } catch (Exception e) {
            if(BaseConstant.IS_SHOWLOG) {
                e.printStackTrace();
            }

        }
        return mResult;
    }
}
