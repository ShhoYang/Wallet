package com.highstreet.wallet.task.FetchTask;

import android.text.TextUtils;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.model.StarNameAccount;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.req.ReqStarNameByOwner;
import com.highstreet.wallet.network.res.ResIovStarNameAccount;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;

import java.util.ArrayList;

import retrofit2.Response;

public class StarNameMyAccountTask extends CommonTask {

    private BaseChain   mChain;
    private Account     mAccount;
    private int         mOffset = 1;
    private boolean     mBreak = false;
    private ArrayList<StarNameAccount> resultData = new ArrayList<>();

    public StarNameMyAccountTask(BaseApplication app, TaskListener listener, BaseChain chain, Account account) {
        super(app, listener);
        this.mChain = chain;
        this.mAccount = account;
        this.mResult.taskType   = BaseConstant.TASK_FETCH_MY_STARNAME_ACCOUNT;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        while(!mBreak) {
            ArrayList<StarNameAccount> temp = onDoingJob(mOffset);
            resultData.addAll(temp);
            if (temp.size() == 100) {
                mOffset++;
            } else {
                mBreak = true;
            }
        }
        ArrayList<StarNameAccount> returnValue = new ArrayList<>();
        for (StarNameAccount account:resultData) {
            if (!TextUtils.isEmpty(account.name)) {
                returnValue.add(account);
            }
        }
        mResult.resultData = returnValue;
        return mResult;
    }

    private ArrayList<StarNameAccount> onDoingJob(int offset) {
        ArrayList<StarNameAccount> resultData = new ArrayList<>();
        try {
            if (mChain.equals(BaseChain.IOV_MAIN)) {
                ReqStarNameByOwner req = new ReqStarNameByOwner(mAccount.address, 100, offset);
                Response<ResIovStarNameAccount> response = ApiClient.getIovChain(mApp).getStarnameAccount(req).execute();
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().result != null) {
                        if (response.body().result.accounts != null) {
                            resultData = response.body().result.accounts;
                        }
                    }
                }
            } else if (mChain.equals(BaseChain.IOV_TEST)) {
                ReqStarNameByOwner req = new ReqStarNameByOwner(mAccount.address, 100, offset);
                Response<ResIovStarNameAccount> response = ApiClient.getIovTestChain(mApp).getStarnameAccount(req).execute();
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().result != null) {
                        if (response.body().result.accounts != null) {
                            resultData = response.body().result.accounts;
                        }
                    }
                }
            }

        } catch (Exception e) { }
        return resultData;
    }

}
