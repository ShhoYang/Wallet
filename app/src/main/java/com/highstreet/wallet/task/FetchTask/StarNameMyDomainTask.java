package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.model.StarNameDomain;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.req.ReqStarNameByOwner;
import com.highstreet.wallet.network.res.ResIovStarNameDomain;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;

import java.util.ArrayList;

import retrofit2.Response;

public class StarNameMyDomainTask extends CommonTask {

    private BaseChain   mChain;
    private Account     mAccount;
    private int         mOffset = 1;
    private boolean     mBreak = false;
    private ArrayList<StarNameDomain> resultData = new ArrayList<>();

    public StarNameMyDomainTask(BaseApplication app, TaskListener listener, BaseChain chain, Account account) {
        super(app, listener);
        this.mChain = chain;
        this.mAccount = account;
        this.mResult.taskType   = BaseConstant.TASK_FETCH_MY_STARNAME_DOMAIN;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        while(!mBreak) {
            ArrayList<StarNameDomain> temp = onDoingJob(mOffset);
            resultData.addAll(temp);
            if (temp.size() == 100) {
                mOffset++;
            } else {
                mBreak = true;
            }
        }
        mResult.resultData = resultData;
        return mResult;
    }


    private ArrayList<StarNameDomain> onDoingJob(int offset) {
        ArrayList<StarNameDomain> resultData = new ArrayList<>();
        try {
            if (mChain.equals(BaseChain.IOV_MAIN)) {
                ReqStarNameByOwner req = new ReqStarNameByOwner(mAccount.address, 100, offset);
                Response<ResIovStarNameDomain> response = ApiClient.getIovChain(mApp).getStarnameDomain(req).execute();
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().result != null) {
                        if (response.body().result.Domains != null) {
                            resultData = response.body().result.Domains;
                        }
                    }
                }
            } else if (mChain.equals(BaseChain.IOV_TEST)) {
                ReqStarNameByOwner req = new ReqStarNameByOwner(mAccount.address, 100, offset);
                Response<ResIovStarNameDomain> response = ApiClient.getIovTestChain(mApp).getStarnameDomain(req).execute();
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().result != null) {
                        if (response.body().result.Domains != null) {
                            resultData = response.body().result.Domains;
                        }
                    }
                }
            }

        } catch (Exception e) { }
        return resultData;
    }

}
