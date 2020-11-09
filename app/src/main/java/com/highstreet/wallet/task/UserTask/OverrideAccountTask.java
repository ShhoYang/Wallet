package com.highstreet.wallet.task.UserTask;

import com.highstreet.wallet.R;
import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.crypto.CryptoHelper;
import com.highstreet.wallet.crypto.EncResult;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WKey;

import org.bitcoinj.crypto.DeterministicKey;

public class OverrideAccountTask extends CommonTask {
    private BaseChain mBaseChain;
    private Account mAccount;
    private Boolean mKavaNewPath;

    public OverrideAccountTask(BaseApplication app, BaseChain chain, Account account, TaskListener listener, boolean bip44) {
        super(app, listener);
        this.mBaseChain = chain;
        this.mAccount = account;
        this.mKavaNewPath = bip44;
        this.mResult.taskType = BaseConstant.TASK_OVERRIDE_ACCOUNT;
    }

    /**
     *
     * @param strings
     *  strings[0] : path
     *  strings[1] : entorpy
     *  strings[2] : word size
     *
     * @return
     */
    @Override
    protected TaskResult doInBackground(String... strings) {
        try {

            Account oAccount = onModAccount(mAccount, strings[1], strings[0], strings[02]);
            long id = mApp.getBaseDao().onOverrideAccount(oAccount);
            if(id > 0) {
                mResult.isSuccess = true;
                mApp.getBaseDao().setLastUser(oAccount.id);

            } else {
                mResult.errorMsg = "Override error";
                mResult.errorCode = 7002;
            }

        } catch (Exception e){

        }
        return mResult;
    }

    private Account onModAccount(Account account, String entropy, String path, String msize) {
        DeterministicKey dKey   = WKey.getKeyWithPathfromEntropy(BaseChain.getChain(mAccount.baseChain), entropy, Integer.parseInt(path), mKavaNewPath);
        EncResult encR          = CryptoHelper.doEncryptData(mApp.getString(R.string.key_mnemonic)+ account.uuid, entropy, false);
        account.address         = WKey.getDpAddress(BaseChain.getChain(account.baseChain), dKey.getPublicKeyAsHex());
        account.hasPrivateKey   = true;
        account.resource        = encR.getEncDataString();
        account.spec            = encR.getIvDataString();
        account.fromMnemonic    = true;
        account.path            = path;
        account.msize           = Integer.parseInt(msize);
        account.newBip44        = mKavaNewPath;
        return account;
    }
}

