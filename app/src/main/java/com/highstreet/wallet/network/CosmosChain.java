package com.highstreet.wallet.network;

import com.google.gson.JsonObject;
import com.highstreet.wallet.model.type.Coin;
import com.highstreet.wallet.network.req.ReqBroadCast;
import com.highstreet.wallet.network.res.ResBlockInfo;
import com.highstreet.wallet.network.res.ResBroadTx;
import com.highstreet.wallet.network.res.ResLcdAccountInfo;
import com.highstreet.wallet.network.res.ResLcdBondings;
import com.highstreet.wallet.network.res.ResLcdInflation;
import com.highstreet.wallet.network.res.ResLcdProposal;
import com.highstreet.wallet.network.res.ResLcdProposalTally;
import com.highstreet.wallet.network.res.ResLcdProposalVoted;
import com.highstreet.wallet.network.res.ResLcdProposals;
import com.highstreet.wallet.network.res.ResLcdProposer;
import com.highstreet.wallet.network.res.ResLcdRedelegate;
import com.highstreet.wallet.network.res.ResLcdRewardFromVal;
import com.highstreet.wallet.network.res.ResLcdSingleBonding;
import com.highstreet.wallet.network.res.ResLcdSingleUnBonding;
import com.highstreet.wallet.network.res.ResLcdSingleValidator;
import com.highstreet.wallet.network.res.ResLcdUnBondings;
import com.highstreet.wallet.network.res.ResLcdValidators;
import com.highstreet.wallet.network.res.ResLcdWithDrawAddress;
import com.highstreet.wallet.network.res.ResMyVote;
import com.highstreet.wallet.network.res.ResProvisions;
import com.highstreet.wallet.network.res.ResStakingPool;
import com.highstreet.wallet.network.res.ResTxInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CosmosChain {

    @GET("/node_info")
    Call<JsonObject> getNodeInfo();

    @GET("/auth/accounts/{address}")
    Call<ResLcdAccountInfo> getAccountInfo(@Path("address") String address);

    @GET("/txs/{hash}")
    Call<ResTxInfo> getSearchTx(@Path("hash") String hash);

    @GET("/blocks/{height}")
    Call<ResBlockInfo> getSearchBlock(@Path("height") String height);

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

    @GET("/distribution/delegators/{address}/rewards")
    Call<ArrayList<Coin>> getTotalRewards(@Path("address") String address);

    @GET("/distribution/delegators/{delegatorAddr}/rewards/{validatorAddr}")
    Call<ResLcdRewardFromVal> getRewardFromValidator(@Path("delegatorAddr") String delegatorAddr, @Path("validatorAddr") String validatorAddr);

    @GET("/distribution/delegators/{address}/withdraw_address")
    Call<ResLcdWithDrawAddress> getWithdrawAddress(@Path("address") String address);

    //Validator details
    @GET("/staking/validators/{validatorAddr}")
    Call<ResLcdSingleValidator> getValidatorDetail(@Path("validatorAddr") String validatorAddr);

    @GET("/staking/delegators/{address}/delegations/{validatorAddr}")
    Call<ResLcdSingleBonding> getBonding(@Path("address") String address, @Path("validatorAddr") String validatorAddr);

    @GET("/staking/delegators/{address}/unbonding_delegations/{validatorAddr}")
    Call<ResLcdSingleUnBonding> getUnbonding(@Path("address") String address, @Path("validatorAddr") String validatorAddr);


    //ReDelegate History
    @GET("/staking/redelegations")
    Call<ResLcdRedelegate> getRedelegateAllHistory(@Query("delegator") String delegator, @Query("validator_from") String validator_from, @Query("validator_to") String validator_to);

    @GET("/staking/redelegations")
    Call<ResLcdRedelegate> getRedelegateHistory(@Query("delegator") String delegator, @Query("validator_to") String validator_to);


    @GET("/minting/inflation")
    Call<ResLcdInflation> getInflation();

    @GET("/minting/annual-provisions")
    Call<ResProvisions> getProvisions();

    @GET("/staking/pool")
    Call<ResStakingPool> getStakingPool();

    //Proposals
    @GET("/gov/proposals")
    Call<ResLcdProposals> getProposalList();

    @GET("/gov/proposals/{proposalId}/proposer")
    Call<ResLcdProposer> getProposer(@Path("proposalId") String proposalId);

    @GET("/gov/proposals/{proposalId}")
    Call<ResLcdProposal> getProposalDetail(@Path("proposalId") String proposalId);

    @GET("/gov/proposals/{proposalId}/votes")
    Call<ResLcdProposalVoted> getVotedList(@Path("proposalId") String proposalId);

    @GET("/gov/proposals/{proposalId}/tally")
    Call<ResLcdProposalTally> getTally(@Path("proposalId") String proposalId);

    @GET("/gov/proposals/{proposalId}/votes/{address}")
    Call<ResMyVote> getMyVote(@Path("proposalId") String proposalId, @Path("address") String address);

    //Broadcast Tx
    @POST("/txs")
    Call<ResBroadTx> broadTx(@Body ReqBroadCast data);

}
