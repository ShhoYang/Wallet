package com.highstreet.wallet.network.res;

import com.google.gson.annotations.SerializedName;
import com.highstreet.wallet.model.KavaCDP;
import com.highstreet.wallet.model.type.Coin;

import java.util.ArrayList;

public class ResCdpList {

    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public ArrayList<Result> result;

    public class Result {

        @SerializedName("cdp")
        public KavaCDP cdp;

        @SerializedName("collateral_value")
        public Coin collateral_value;

        @SerializedName("collateralization_ratio")
        public String collateralization_ratio;
    }
}
