package com.highstreet.wallet.task.UserTask;

import com.highstreet.wallet.R;
import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.crypto.CryptoHelper;
import com.highstreet.wallet.dao.Password;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;

public class DeleteUserTask extends CommonTask {

    public DeleteUserTask(BaseApplication app, TaskListener listener) {
        super(app, listener);
        this.mResult.taskType = BaseConstant.TASK_DELETE_USER;
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
            mResult.isSuccess = true;
        }
//        WLog.w("CheckPasswordTask : " + mResult.isSuccess);
        return mResult;
    }
}
