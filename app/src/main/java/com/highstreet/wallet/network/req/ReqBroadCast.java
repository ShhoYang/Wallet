package com.highstreet.wallet.network.req;

import com.google.gson.annotations.SerializedName;
import com.highstreet.wallet.model.StdTx;

public class ReqBroadCast {

    @SerializedName("mode")
    public String returns;

    @SerializedName("tx")
    public StdTx.Value tx;

}
