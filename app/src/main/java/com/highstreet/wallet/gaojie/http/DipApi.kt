package com.highstreet.wallet.gaojie.http

import com.highstreet.wallet.gaojie.model.ProposalOpinion
import com.highstreet.wallet.gaojie.model.dip.Validator
import com.highstreet.wallet.gaojie.model.dip.*
import com.highstreet.wallet.network.req.ReqBroadCast
import io.reactivex.Observable
import retrofit2.http.*

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */

interface DipApi {

    /**
     * 水龙头
     */
    @GET
    fun test(@Url url: String): Observable<Any>

    /**
     * 账户信息
     */
    @GET("auth/accounts/{address}")
    fun account(@Path("address") address: String): Observable<BaseBean<AccountInfo>>

    /**
     * 转账，委托
     */
    @POST("txs")
    fun txs(@Body ReqBroadCast: ReqBroadCast): Observable<Tx>

    /**
     * 入账记录
     * txs?transfer.recipient=地址page=1&limit=100
     */
    @GET("txs")
    fun transactionInRecord(@Query("transfer.recipient") address: String, @Query("page") page: Int, @Query("limit") pageSize: Int): Observable<Transaction>

    /**
     * 出账记录
     * txs?message.action=send&message.sender=地址&page=1&limit=100
     */
    @GET("txs")
    fun transactionOutRecord(@Query("message.sender") address: String, @Query("page") page: Int, @Query("limit") pageSize: Int, @Query("message.action") action: String = "send"): Observable<Transaction>

    /**
     * 委托和验证人信息
     */
    @GET("staking/delegators/{address}/delegations")
    fun delegatorAndValidatorInfo(@Path("address") address: String): Observable<BaseBean<DelegationInfo>>

    /**
     * 委托信息
     */
    @GET("staking/delegators/{address}/delegations")
    fun delegations(@Path("address") address: String, @Query("page") page: Int, @Query("limit") pageSize: Int): Observable<BaseBean<ArrayList<DelegationInfo>>>

    /**
     * 正在解除的委托信息
     */
    @GET("staking/delegators/{address}/unbonding_delegations")
    fun unBondingDelegations(@Path("address") address: String, @Query("page") page: Int, @Query("limit") pageSize: Int): Observable<BaseBean<ArrayList<DelegationInfo>>>

    /**
     * staking交易
     */
    @GET("staking/delegators/{address}/txs")
    fun delegationTransactionRecord(@Path("address") address: String, @Query("type") type: String, @Query("page") page: Int, @Query("limit") pageSize: Int): Observable<BaseBean<ArrayList<Transaction>>>

    /**
     * 所有验证人
     */
    @GET("staking/validators")
    fun validators(@Query("page") page: Int, @Query("limit") pageSize: Int): Observable<BaseBean<ArrayList<Validator>>>

    /**
     * 获取验证人信息
     */
    @GET("staking/validators/{validatorAddress}")
    fun validatorDetail(@Path("validatorAddress") validatorAddress: String): Observable<BaseBean<Validator>>

    /**
     * 查询收益
     */
    @GET("distribution/delegators/{address}/rewards")
    fun rewards(@Path("address") address: String): Observable<BaseBean<Rewards>>

    /**
     * 根据委托人查询收益
     */
    @GET("distribution/delegators/{address}/rewards/{validatorAddress}")
    fun rewardsByValidator(@Path("address") address: String, @Path("validatorAddress") validatorAddress: String): Observable<BaseBean<ArrayList<Coin>>>

    /**
     * 提案
     */
    @GET("gov/proposals")
    fun proposals(): Observable<BaseBean<ArrayList<Proposal>>>

    /**
     * 提案详情
     */
    @GET("gov/proposals/{proposalId}")
    fun proposalDetail(@Path("proposalId") proposalId: String): Observable<BaseBean<Proposal>>

    /**
     * 投票比例
     */
    @GET("gov/proposals/{proposalId}/tally")
    fun votingRate(@Path("proposalId") proposalId: String): Observable<BaseBean<FinalTallyResult>>

    /**
     * 我的意见
     */
    @GET("gov/proposals/{proposalId}/votes/{voter}")
    fun proposalOpinion(@Path("proposalId") proposalId: String, @Path("voter") voter: String): Observable<BaseBean<ProposalOpinion>>
}