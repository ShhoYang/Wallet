package com.highstreet.wallet.network;

import com.highstreet.wallet.network.req.ReqBroadCast;
import com.highstreet.wallet.network.res.ResBroadTx;
import com.highstreet.wallet.network.res.ResLcdAccountInfo;
import com.highstreet.wallet.network.res.ResLcdBondings;
import com.highstreet.wallet.network.res.ResLcdInflation;
import com.highstreet.wallet.network.res.ResLcdRedelegate;
import com.highstreet.wallet.network.res.ResLcdRewardFromVal;
import com.highstreet.wallet.network.res.ResLcdSingleBonding;
import com.highstreet.wallet.network.res.ResLcdSingleUnBonding;
import com.highstreet.wallet.network.res.ResLcdSingleValidator;
import com.highstreet.wallet.network.res.ResLcdUnBondings;
import com.highstreet.wallet.network.res.ResLcdValidators;
import com.highstreet.wallet.network.res.ResLcdWithDrawAddress;
import com.highstreet.wallet.network.res.ResProvisions;
import com.highstreet.wallet.network.res.ResStakingPool;
import com.highstreet.wallet.network.res.ResTxInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BandChain {

    @GET("/auth/accounts/{address}")
    Call<ResLcdAccountInfo> getAccountInfo(@Path("address") String address);

    @GET("/txs/{hash}")
    Call<ResTxInfo> getSearchTx(@Path("hash") String hash);

    @GET("/staking/validators?status=bonded")
    Call<ResLcdValidators> getValidatorDetailList();

    @GET("/staking/validators?status=unbonding")
    Call<ResLcdValidators> getUnBondingValidatorDetailList();

    @GET("/staking/validators?status=unbonded")
    Call<ResLcdValidators> getUnBondedValidatorDetailList();

    @GET("/staking/delegators/{address}/delegations")
    Call<ResLcdBondings> getBondingList(@Path("address") String address);

    @GET("/staking/delegators/{address}/unbonding_delegations")
    Call<ResLcdUnBondings> getUnBondingList(@Path("address") String address);

    @GET("/distribution/delegators/{delegatorAddr}/rewards/{validatorAddr}")
    Call<ResLcdRewardFromVal> getRewardFromValidator(@Path("delegatorAddr") String delegatorAddr, @Path("validatorAddr") String validatorAddr);

    @GET("/minting/inflation")
    Call<ResLcdInflation> getInflation();

    @GET("/minting/annual-provisions")
    Call<ResProvisions> getProvisions();

    @GET("/staking/pool")
    Call<ResStakingPool> getStakingPool();

    @GET("/distribution/delegators/{address}/withdraw_address")
    Call<ResLcdWithDrawAddress> getWithdrawAddress(@Path("address") String address);


    @GET("/staking/validators/{validatorAddr}")
    Call<ResLcdSingleValidator> getValidatorDetail(@Path("validatorAddr") String validatorAddr);

    @GET("/staking/delegators/{address}/delegations/{validatorAddr}")
    Call<ResLcdSingleBonding> getBonding(@Path("address") String address, @Path("validatorAddr") String validatorAddr);

    @GET("/staking/delegators/{address}/unbonding_delegations/{validatorAddr}")
    Call<ResLcdSingleUnBonding> getUnbonding(@Path("address") String address, @Path("validatorAddr") String validatorAddr);

    @GET("/staking/redelegations")
    Call<ResLcdRedelegate> getRedelegateHistory(@Query("delegator") String delegator, @Query("validator_to") String validator_to);

    @GET("/staking/redelegations")
    Call<ResLcdRedelegate> getRedelegateAllHistory(@Query("delegator") String delegator, @Query("validator_from") String validator_from, @Query("validator_to") String validator_to);


    //Broadcast Tx
    @POST("/txs")
    Call<ResBroadTx> broadTx(@Body ReqBroadCast data);
}
