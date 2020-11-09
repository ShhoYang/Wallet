package com.highstreet.wallet.network.res;

import com.google.gson.annotations.SerializedName;
import com.highstreet.wallet.model.type.Coin;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ResCdpDepositStatus {

    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public ArrayList<Result> result;

    public class Result {

        @SerializedName("cdp_id")
        public String cdp_id;

        @SerializedName("depositor")
        public String depositor;

        @SerializedName("amount")
        public Coin amount;

    }

    public BigDecimal getSelfDeposit(String address) {
        for (Result deposit:result) {
            if (deposit.depositor.equals(address)) {
                return new BigDecimal(deposit.amount.amount);
            }
        }
        return BigDecimal.ZERO;
    }

}
