package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.req.ReqPushAlarm;
import com.highstreet.wallet.network.res.ResPushAlarm;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;

import retrofit2.Response;

public class PushUpdateTask extends CommonTask {

    private Account mAccount;
    private String mPushToken;
    private boolean mEnable;

    public PushUpdateTask(BaseApplication app, TaskListener listener, Account account, String token, boolean enable) {
        super(app, listener);
        this.mAccount = account;
        this.mPushToken = token;
        this.mEnable = enable;
        this.mResult.taskType = BaseConstant.TASK_PUSH_STATUS_UPDATE;
        this.mResult.resultData = enable;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {

            ReqPushAlarm reqPushAlarm = new ReqPushAlarm();
            if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.COSMOS_MAIN)) {
                reqPushAlarm.chain_id = 1;
            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.IRIS_MAIN)) {
                reqPushAlarm.chain_id = 2;
            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.KAVA_MAIN)) {
                reqPushAlarm.chain_id = 3;
            }
            reqPushAlarm.device_type = "android";
            reqPushAlarm.address = mAccount.address;
            reqPushAlarm.alarm_token = mPushToken;
            reqPushAlarm.alarm_status = mEnable;

            Response<ResPushAlarm> response = ApiClient.getCosmostation(mApp).updateAlarm(reqPushAlarm).execute();
            if (response.isSuccessful() && response.body() != null && response.body().result == true) {
                mResult.isSuccess = true;
            }

        } catch (Exception e) {
            if(BaseConstant.IS_SHOWLOG) e.printStackTrace();

        }
        return mResult;
    }
}
