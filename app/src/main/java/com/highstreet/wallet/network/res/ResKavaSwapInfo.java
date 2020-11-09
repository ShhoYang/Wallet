package com.highstreet.wallet.network.res;

import com.google.gson.annotations.SerializedName;
import com.highstreet.wallet.model.type.Coin;

import java.util.ArrayList;

public class ResKavaSwapInfo {

    public static final String STATUS_NULL      = "NULL";
    public static final String STATUS_OPEN      = "Open";
    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_EXPIRED   = "Expired";

    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public Result result;

    public class Result {

        @SerializedName("amount")
        public ArrayList<Coin> amount;

        @SerializedName("sender")
        public String sender;

        @SerializedName("status")
        public String status;

        @SerializedName("direction")
        public String direction;

    }
}
