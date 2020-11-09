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
public class GenerateAccountTask extends CommonTask {
    private BaseChain mBaseChain;
    private Boolean mKavaNewPath;

    public GenerateAccountTask(BaseApplication app, BaseChain basechain, TaskListener listener, boolean bip44) {
        super(app, listener);
        this.mBaseChain = basechain;
        this.mKavaNewPath = bip44;
        this.mResult.taskType = BaseConstant.TASK_INIT_ACCOUNT;
    }


    /**
     *
     * @param strings
     *  strings[0] : path
     *  strings[1] : entorpy seed
     *  strings[2] : word size
     *
     * @return
     */
    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            long id = mApp.getBaseDao().onInsertAccount(onGenAccount(strings[1], strings[0], strings[2]));
            if(id > 0) {
                mResult.isSuccess = true;
                mApp.getBaseDao().setLastUser(id);

            } else {
                mResult.errorMsg = "Already existed account";
                mResult.errorCode = 7001;
            }

        } catch (Exception e){

        }
        return mResult;
    }



    private Account onGenAccount(String entropy, String path, String msize) {
        Account newAccount          = Account.getNewInstance();
        DeterministicKey dKey       = WKey.getKeyWithPathfromEntropy(mBaseChain, entropy, Integer.parseInt(path), mKavaNewPath);
        EncResult encR              = CryptoHelper.doEncryptData(mApp.getString(R.string.key_mnemonic)+ newAccount.uuid, entropy, false);
        newAccount.address          = WKey.getDpAddress(mBaseChain, dKey.getPublicKeyAsHex());
        newAccount.baseChain        = mBaseChain.getChain();
        newAccount.hasPrivateKey    = true;
        newAccount.resource         = encR.getEncDataString();
        newAccount.spec             = encR.getIvDataString();
        newAccount.fromMnemonic     = true;
        newAccount.path             = path;
        newAccount.msize            = Integer.parseInt(msize);
        newAccount.importTime       = System.currentTimeMillis();
        newAccount.newBip44         = mKavaNewPath;
        return newAccount;

    }

}
