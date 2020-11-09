package com.highstreet.wallet.task.UserTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;

public class GenerateEmptyAccountTask extends CommonTask {

    public GenerateEmptyAccountTask(BaseApplication app, TaskListener listener) {
        super(app, listener);
        this.mResult.taskType = BaseConstant.TASK_INIT_EMPTY_ACCOUNT;
    }

    /**
     *
     * @param strings
     *  strings[0] : chainType
     *  strings[1] : address
     * @return
     */
    @Override
    protected TaskResult doInBackground(String... strings) {

        long id = mApp.getBaseDao().onInsertAccount(onGenEmptyAccount(strings[0], strings[1]));
        if(id > 0) {
            mResult.isSuccess = true;
            mApp.getBaseDao().setLastUser(id);
        } else {
            mResult.errorMsg = "Already existed account";
            mResult.errorCode = 7001;
        }

        return mResult;
    }

    private Account onGenEmptyAccount(String chainType, String address) {
        Account newAccount          = Account.getNewInstance();
        newAccount.address          = address;
        newAccount.baseChain        = chainType;
        newAccount.hasPrivateKey    = false;
        newAccount.fromMnemonic     = false;
        newAccount.importTime       = System.currentTimeMillis();

        return newAccount;
    }
}
