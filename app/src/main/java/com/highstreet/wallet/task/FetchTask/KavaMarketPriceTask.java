package com.highstreet.wallet.task.FetchTask;

import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResKavaMarketPrice;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WLog;

import retrofit2.Response;

public class KavaMarketPriceTask extends CommonTask {

    private BaseChain mChain;
    private String mMarket;

    public KavaMarketPriceTask(BaseApplication app, TaskListener listener, BaseChain chain, String market) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_KAVA_TOKEN_PRICE;
        this.mChain = chain;
        this.mMarket = market;

    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BaseChain.KAVA_MAIN)) {
                Response<ResKavaMarketPrice> response = ApiClient.getKavaChain(mApp).getPrice(mMarket).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;

                } else {
                    WLog.w("KavaMarketPriceTask : NOk");
                }

            } else if (mChain.equals(BaseChain.KAVA_TEST)) {
                Response<ResKavaMarketPrice> response = ApiClient.getKavaTestChain(mApp).getPrice(mMarket).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;

                } else {
                    WLog.w("KavaMarketPriceTask : NOk");
                }
            }

        } catch (Exception e) {
            WLog.w("KavaMarketPriceTask Error " + e.getMessage());
        }
        return mResult;
    }
}