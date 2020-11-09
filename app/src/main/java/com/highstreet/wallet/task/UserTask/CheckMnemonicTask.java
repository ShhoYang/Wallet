package com.highstreet.wallet.task.UserTask;

import com.highstreet.wallet.R;
import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.crypto.CryptoHelper;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.dao.Password;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;

public class CheckMnemonicTask extends CommonTask {

    private Account mAccount;

    public CheckMnemonicTask(BaseApplication app, TaskListener listener, Account account) {
        super(app, listener);
        this.mResult.taskType = BaseConstant.TASK_CHECK_MNEMONIC;
        this.mAccount = account;
    }

    /**
     *
     * @param strings
     *  strings[0] : password
     *
     * @return
     */
    @Override
    protected TaskResult doInBackground(String... strings) {
        Password checkPw = mApp.getBaseDao().onSelectPassword();
        if(!CryptoHelper.verifyData(strings[0], checkPw.resource, mApp.getString(R.string.key_password))) {
            mResult.isSuccess = false;
            mResult.errorCode = BaseConstant.ERROR_CODE_INVALID_PASSWORD;
            return mResult;
        } else {
            String entropy = CryptoHelper.doDecryptData(mApp.getString(R.string.key_mnemonic) + mAccount.uuid, mAccount.resource, mAccount.spec);
            mResult.resultData = entropy;
            mResult.isSuccess = true;
        }
        return mResult;
    }
}
