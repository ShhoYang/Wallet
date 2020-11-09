package com.highstreet.wallet.dao;

import com.highstreet.wallet.model.type.Validator;

import java.math.BigDecimal;

public class BondingState {
    public Long         accountId;
    public String       validatorAddress;
    public BigDecimal   shares;
    public Long         fetchTime;

    public BondingState() {
    }

    public BondingState(Long accountId, String validatorAddress, BigDecimal shares, Long fetchTime) {
        this.accountId = accountId;
        this.validatorAddress = validatorAddress;
        this.shares = shares;
        this.fetchTime = fetchTime;
    }


    public BigDecimal getBondingAmount(Validator validator) {
        BigDecimal result = BigDecimal.ZERO;
        try {
            result = new BigDecimal(validator.tokens).multiply(shares).divide(new BigDecimal(validator.delegator_shares), 0, BigDecimal.ROUND_DOWN);

        }catch (Exception e) {
        }
        return result;
    }

}