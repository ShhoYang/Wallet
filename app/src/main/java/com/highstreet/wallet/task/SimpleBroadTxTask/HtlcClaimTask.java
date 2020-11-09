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
import com.highstreet.wallet.cosmos.MsgGenerator;
import com.highstreet.wallet.crypto.CryptoHelper;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.model.type.Fee;
import com.highstreet.wallet.model.type.Msg;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.req.ReqBroadCast;
import com.highstreet.wallet.network.res.ResBnbAccountInfo;
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
import java.util.List;

import retrofit2.Response;

import static com.highstreet.wallet.base.BaseConstant.IS_SHOWLOG;
import static com.highstreet.wallet.base.BaseConstant.TASK_GEN_TX_HTLC_CLAIM;

public class HtlcClaimTask extends CommonTask {

    private Account         mReceiveAccount;
    private BaseChain       mReceiveChain;
    private Fee             mClaimFee;
    private String          mExpectedSwapId;
    private String          mRandomNumber;

    public HtlcClaimTask(BaseApplication app, TaskListener listener, Account recipient, BaseChain receiveChain, Fee claimFee, String expectedSwapId, String randomNumber) {
        super(app, listener);
        this.mReceiveAccount = recipient;
        this.mReceiveChain = receiveChain;
        this.mClaimFee = claimFee;
        this.mExpectedSwapId = expectedSwapId;
        this.mRandomNumber = randomNumber;
        this.mResult.taskType = TASK_GEN_TX_HTLC_CLAIM;
    }


    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mReceiveChain.equals(BaseChain.BNB_MAIN)) {
                Response<ResBnbAccountInfo> response = ApiClient.getBnbChain(mApp).getAccountInfo(mReceiveAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromBnbLcd(mReceiveAccount.id, response.body()));
                mApp.getBaseDao().onUpdateBalances(mReceiveAccount.id, WUtil.getBalancesFromBnbLcd(mReceiveAccount.id, response.body()));
                mReceiveAccount = mApp.getBaseDao().onSelectAccount(""+mReceiveAccount.id);

                String entropy = CryptoHelper.doDecryptData(mApp.getString(R.string.key_mnemonic) + mReceiveAccount.uuid, mReceiveAccount.resource, mReceiveAccount.spec);
                DeterministicKey deterministicKey = WKey.getKeyWithPathfromEntropy(BaseChain.getChain(mReceiveAccount.baseChain), entropy, Integer.parseInt(mReceiveAccount.path), mReceiveAccount.newBip44);

                Wallet wallet = new Wallet(deterministicKey.getPrivateKeyAsHex(), BinanceDexEnvironment.PROD);
                wallet.setAccountNumber(mReceiveAccount.accountNumber);
                wallet.setSequence(Long.valueOf(mReceiveAccount.sequenceNumber));

                mExpectedSwapId = mExpectedSwapId.toLowerCase();
                mRandomNumber = mRandomNumber.toLowerCase();
                BinanceDexApiRestClient client = BinanceDexApiClientFactory.newInstance().newRestClient(BinanceDexEnvironment.PROD.getBaseUrl());
                TransactionOption options = new TransactionOption(mApp.getString(R.string.str_claim_swap_memo_c)  , 82, null);
                List<TransactionMetadata> resp = client.claimHtlt(mExpectedSwapId, WUtil.HexStringToByteArray(mRandomNumber), wallet, options, true);
                if (resp.get(0).isOk()) {
                    if (IS_SHOWLOG) WLog.w("BNB_MAIN Claim suceess txhash " + resp.get(0).getHash());
                    mResult.resultData = resp.get(0).getHash();
                    mResult.isSuccess = true;

                } else {
                    if (IS_SHOWLOG) WLog.w("BNB_MAIN Claim error " + resp.get(0).getCode() + "  " + resp.get(0).getLog());
                    mResult.errorCode = resp.get(0).getCode();
                    mResult.errorMsg = resp.get(0).getLog();
                    mResult.isSuccess = false;
                }

            } else if (mReceiveChain.equals(BaseChain.BNB_TEST)) {
                Response<ResBnbAccountInfo> response = ApiClient.getBnbTestChain(mApp).getAccountInfo(mReceiveAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromBnbLcd(mReceiveAccount.id, response.body()));
                mApp.getBaseDao().onUpdateBalances(mReceiveAccount.id, WUtil.getBalancesFromBnbLcd(mReceiveAccount.id, response.body()));
                mReceiveAccount = mApp.getBaseDao().onSelectAccount(""+mReceiveAccount.id);

                String entropy = CryptoHelper.doDecryptData(mApp.getString(R.string.key_mnemonic) + mReceiveAccount.uuid, mReceiveAccount.resource, mReceiveAccount.spec);
                DeterministicKey deterministicKey = WKey.getKeyWithPathfromEntropy(BaseChain.getChain(mReceiveAccount.baseChain), entropy, Integer.parseInt(mReceiveAccount.path), mReceiveAccount.newBip44);

                Wallet wallet = new Wallet(deterministicKey.getPrivateKeyAsHex(), BinanceDexEnvironment.TEST_NET);
                wallet.setAccountNumber(mReceiveAccount.accountNumber);
                wallet.setSequence(Long.valueOf(mReceiveAccount.sequenceNumber));

                mExpectedSwapId = mExpectedSwapId.toLowerCase();
                mRandomNumber = mRandomNumber.toLowerCase();
                BinanceDexApiRestClient client = BinanceDexApiClientFactory.newInstance().newRestClient(BinanceDexEnvironment.TEST_NET.getBaseUrl());
                TransactionOption options = new TransactionOption(mApp.getString(R.string.str_claim_swap_memo_c)  , 82, null);
                List<TransactionMetadata> resp = client.claimHtlt(mExpectedSwapId, WUtil.HexStringToByteArray(mRandomNumber), wallet, options, true);
                if (resp.get(0).isOk()) {
                    if (IS_SHOWLOG) WLog.w("BNB_TEST Claim suceess txhash " + resp.get(0).getHash());
                    mResult.resultData = resp.get(0).getHash();
                    mResult.isSuccess = true;

                } else {
                    if (IS_SHOWLOG) WLog.w("BNB_TEST Claim error " + resp.get(0).getCode() + "  " + resp.get(0).getLog());
                    mResult.errorCode = resp.get(0).getCode();
                    mResult.errorMsg = resp.get(0).getLog();
                    mResult.isSuccess = false;
                }


            } else if (mReceiveChain.equals(BaseChain.KAVA_MAIN)) {
                Response<ResLcdKavaAccountInfo> response = ApiClient.getKavaChain(mApp).getAccountInfo(mReceiveAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromKavaLcd(mReceiveAccount.id, response.body()));
                mReceiveAccount = mApp.getBaseDao().onSelectAccount(""+mReceiveAccount.id);

                String entropy = CryptoHelper.doDecryptData(mApp.getString(R.string.key_mnemonic) + mReceiveAccount.uuid, mReceiveAccount.resource, mReceiveAccount.spec);
                DeterministicKey deterministicKey = WKey.getKeyWithPathfromEntropy(BaseChain.getChain(mReceiveAccount.baseChain), entropy, Integer.parseInt(mReceiveAccount.path), mReceiveAccount.newBip44);

                Msg claimSwapMsg = MsgGenerator.genClaimAtomicSwap(mReceiveAccount.address, mExpectedSwapId, mRandomNumber, mReceiveChain);
                ArrayList<Msg> msgs= new ArrayList<>();
                msgs.add(claimSwapMsg);

                ReqBroadCast reqBroadCast = MsgGenerator.getBraodcaseReq(mReceiveAccount, msgs, mClaimFee, mApp.getString(R.string.str_claim_swap_memo_c), deterministicKey);
//            WLog.w("reqBroadCast : " +  WUtil.prettyPrinter(reqBroadCast));
                Response<ResBroadTx> claimRes = ApiClient.getKavaChain(mApp).broadTx(reqBroadCast).execute();
                if(claimRes.isSuccessful() && claimRes.body() != null) {
                    if (claimRes.body().txhash != null) {
                        if (IS_SHOWLOG) WLog.w("KAVA_MAIN Claim suceess txhash " + claimRes.body().txhash);
                        mResult.resultData = claimRes.body().txhash;
                        mResult.isSuccess = true;
                    }
                    if(claimRes.body().code != null) {
                        if (IS_SHOWLOG) WLog.w("KAVA_MAIN Claim error " + mResult.errorCode + "  " + mResult.errorMsg);
                        mResult.errorCode = claimRes.body().code;
                        mResult.errorMsg = claimRes.body().raw_log;
                        mResult.isSuccess = false;
                    }

                } else {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                }

            } else if (mReceiveChain.equals(BaseChain.KAVA_TEST)) {
                Response<ResLcdKavaAccountInfo> response = ApiClient.getKavaTestChain(mApp).getAccountInfo(mReceiveAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromKavaLcd(mReceiveAccount.id, response.body()));
                mReceiveAccount = mApp.getBaseDao().onSelectAccount(""+mReceiveAccount.id);

                String entropy = CryptoHelper.doDecryptData(mApp.getString(R.string.key_mnemonic) + mReceiveAccount.uuid, mReceiveAccount.resource, mReceiveAccount.spec);
                DeterministicKey deterministicKey = WKey.getKeyWithPathfromEntropy(BaseChain.getChain(mReceiveAccount.baseChain), entropy, Integer.parseInt(mReceiveAccount.path), mReceiveAccount.newBip44);

                Msg claimSwapMsg = MsgGenerator.genClaimAtomicSwap(mReceiveAccount.address, mExpectedSwapId, mRandomNumber, mReceiveChain);
                ArrayList<Msg> msgs= new ArrayList<>();
                msgs.add(claimSwapMsg);

                ReqBroadCast reqBroadCast = MsgGenerator.getBraodcaseReq(mReceiveAccount, msgs, mClaimFee, mApp.getString(R.string.str_claim_swap_memo_c), deterministicKey);
//            WLog.w("reqBroadCast : " +  WUtil.prettyPrinter(reqBroadCast));
                Response<ResBroadTx> claimRes = ApiClient.getKavaTestChain(mApp).broadTx(reqBroadCast).execute();
                if(claimRes.isSuccessful() && claimRes.body() != null) {
                    if (claimRes.body().txhash != null) {
                        if (IS_SHOWLOG) WLog.w("KAVA_TEST Claim suceess txhash " + claimRes.body().txhash);
                        mResult.resultData = claimRes.body().txhash;
                        mResult.isSuccess = true;
                    }
                    if(claimRes.body().code != null) {
                        if (IS_SHOWLOG) WLog.w("KAVA_TEST Claim error " + mResult.errorCode + "  " + mResult.errorMsg);
                        mResult.errorCode = claimRes.body().code;
                        mResult.errorMsg = claimRes.body().raw_log;
                        mResult.isSuccess = false;
                    }

                } else {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                }
            }

        } catch (Exception e) {
            if(IS_SHOWLOG) e.printStackTrace();
        }
        return mResult;
    }
}
