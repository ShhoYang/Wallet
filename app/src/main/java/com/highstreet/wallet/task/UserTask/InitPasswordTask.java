package com.highstreet.wallet.task.UserTask;

import com.highstreet.wallet.R;
import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.crypto.CryptoHelper;
import com.highstreet.wallet.dao.Password;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;

public class InitPasswordTask extends CommonTask {

    public InitPasswordTask(BaseApplication app, TaskListener listener) {
        super(app, listener);
        this.mResult.taskType = BaseConstant.TASK_INIT_PW;
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
        Password newPw = new Password(CryptoHelper.signData(strings[0], mApp.getString(R.string.key_password)));
        long insert = mApp.getBaseDao().onInsertPassword(newPw);
        if(insert > 0) {
            mResult.isSuccess = true;
        }
        return mResult;
    }
}
