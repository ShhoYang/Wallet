package com.highstreet.wallet.network;

import com.highstreet.wallet.network.req.ReqBroadCast;
import com.highstreet.wallet.network.res.ResBroadTx;
import com.highstreet.wallet.network.res.ResCdpDepositStatus;
import com.highstreet.wallet.network.res.ResCdpList;
import com.highstreet.wallet.network.res.ResCdpOwnerStatus;
import com.highstreet.wallet.network.res.ResCdpParam;
import com.highstreet.wallet.network.res.ResKavaBep3Param;
import com.highstreet.wallet.network.res.ResKavaBep3Param2;
import com.highstreet.wallet.network.res.ResKavaIncentiveParam;
import com.highstreet.wallet.network.res.ResKavaIncentiveReward;
import com.highstreet.wallet.network.res.ResKavaMarketPrice;
import com.highstreet.wallet.network.res.ResKavaPriceParam;
import com.highstreet.wallet.network.res.ResKavaSupply;
import com.highstreet.wallet.network.res.ResKavaSwapInfo;
import com.highstreet.wallet.network.res.ResKavaSwapSupply;
import com.highstreet.wallet.network.res.ResKavaSwapSupply2;
import com.highstreet.wallet.network.res.ResLcdBondings;
import com.highstreet.wallet.network.res.ResLcdInflation;
import com.highstreet.wallet.network.res.ResLcdKavaAccountInfo;
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

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface KavaChain {

    @GET("/txs/{hash}")
    Call<ResTxInfo> getSearchTx(@Path("hash") String hash);

    @GET("/auth/accounts/{address}")
    Call<ResLcdKavaAccountInfo> getAccountInfo(@Path("address") String address);

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

    @GET("/staking/validators/{validatorAddr}")
    Call<ResLcdSingleValidator> getValidatorDetail(@Path("validatorAddr") String validatorAddr);

    @GET("/staking/delegators/{address}/delegations/{validatorAddr}")
    Call<ResLcdSingleBonding> getBonding(@Path("address") String address, @Path("validatorAddr") String validatorAddr);

    @GET("/staking/delegators/{address}/unbonding_delegations/{validatorAddr}")
    Call<ResLcdSingleUnBonding> getUnbonding(@Path("address") String address, @Path("validatorAddr") String validatorAddr);

    @GET("/staking/redelegations")
    Call<ResLcdRedelegate> getRedelegateAllHistory(@Query("delegator") String delegator, @Query("validator_from") String validator_from, @Query("validator_to") String validator_to);

    @GET("/staking/redelegations")
    Call<ResLcdRedelegate> getRedelegateHistory(@Query("delegator") String delegator, @Query("validator_to") String validator_to);

    @GET("/distribution/delegators/{address}/withdraw_address")
    Call<ResLcdWithDrawAddress> getWithdrawAddress(@Path("address") String address);

    @GET("/gov/proposals")
    Call<ResLcdProposals> getProposalList();

    @GET("/gov/proposals/{proposalId}")
    Call<ResLcdProposal> getProposalDetail(@Path("proposalId") String proposalId);

    @GET("/gov/proposals/{proposalId}/tally")
    Call<ResLcdProposalTally> getTally(@Path("proposalId") String proposalId);

    @GET("/gov/proposals/{proposalId}/votes")
    Call<ResLcdProposalVoted> getVotedList(@Path("proposalId") String proposalId);

    @GET("/gov/proposals/{proposalId}/votes/{address}")
    Call<ResMyVote> getMyVote(@Path("proposalId") String proposalId, @Path("address") String address);

    @GET("/gov/proposals/{proposalId}/proposer")
    Call<ResLcdProposer> getProposer(@Path("proposalId") String proposalId);

    @GET("/supply/total")
    Call<ResKavaSupply> getSupply();

    @POST("/txs")
    Call<ResBroadTx> broadTx(@Body ReqBroadCast data);



    @GET("/cdp/parameters")
    Call<ResCdpParam> getCdpParams();

    @GET("/cdp/cdps/cdp/{address}/{denom}")
    Call<ResCdpOwnerStatus> getCdpStatusByOwner(@Path("address") String address, @Path("denom") String denom);

    @GET("/cdp/cdps/cdp/deposits/{address}/{denom}")
    Call<ResCdpDepositStatus> getCdpDepositStatus(@Path("address") String address, @Path("denom") String denom);

    @GET("/cdp/cdps/denom/{denom}")
    Call<ResCdpList> getCdpListByDenom(@Path("denom") String denom);

    @GET("/cdp/cdps/ratio/{denom}/{ratio}")
    Call<ResCdpList> getCdpCoinRate(@Path("denom") String denom, @Path("ratio") String ratio);


    @GET("/pricefeed/parameters")
    Call<ResKavaPriceParam> getPriceParam();

    @GET("/pricefeed/price/{market}")
    Call<ResKavaMarketPrice> getPrice(@Path("market") String market);


    @GET("/bep3/parameters")
    Call<ResKavaBep3Param> getSwapParams();

    @GET("/bep3/swap/{swapId}")
    Call<ResKavaSwapInfo> getSwapById(@Path("swapId") String swapId);

    @GET("/bep3/swaps")
    Call<String> getSwaps();

    @GET("/bep3/supplies")
    Call<ResKavaSwapSupply> getSupplies();



    @GET("/incentive/parameters")
    Call<ResKavaIncentiveParam> getIncentiveParams();

    @GET("/incentive/claims/{address}/{denom}")
    Call<ResKavaIncentiveReward> getIncentive(@Path("address") String address, @Path("denom") String denom);





    @GET("/faucet/{address}")
    Call<JSONObject> getFaucet(@Path("address") String address);



    //For Kava testnet 9000
    @GET("/bep3/parameters")
    Call<ResKavaBep3Param2> getSwapParams2();

    @GET("/bep3/supplies")
    Call<ResKavaSwapSupply2> getSupplies2();


}
