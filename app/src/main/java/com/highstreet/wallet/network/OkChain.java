package com.highstreet.wallet.network;

import com.highstreet.wallet.model.type.Validator;
import com.highstreet.wallet.network.req.ReqBroadCast;
import com.highstreet.wallet.network.res.ResBroadTx;
import com.highstreet.wallet.network.res.ResLcdAccountInfo;
import com.highstreet.wallet.network.res.ResOkAccountToken;
import com.highstreet.wallet.network.res.ResOkDeposit;
import com.highstreet.wallet.network.res.ResOkTokenList;
import com.highstreet.wallet.network.res.ResOkWithdraw;
import com.highstreet.wallet.network.res.ResTxInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OkChain {
    @GET("/auth/accounts/{address}")
    Call<ResLcdAccountInfo> getAccountInfo(@Path("address") String address);

    @GET("/accounts/{address}")
    Call<ResOkAccountToken> getAccountToken(@Path("address") String address);

    @GET("/tokens")
    Call<ResOkTokenList> getTokenList();

    @GET("/txs/{hash}")
    Call<ResTxInfo> getSearchTx(@Path("hash") String hash);

    @GET("/staking/validators?status=all")
    Call<ArrayList<Validator>> getValidatorDetailList();

    @GET("/staking/delegators/{address}")
    Call<ResOkDeposit> getDepositInfo(@Path("address") String address);

    @GET("/staking/delegators/{address}/unbonding_delegations")
    Call<ResOkWithdraw> getWithdrawInfo(@Path("address") String address);










    //Broadcast Tx
    @POST("/txs")
    Call<ResBroadTx> broadTx(@Body ReqBroadCast data);
}
