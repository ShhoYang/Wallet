package com.highstreet.wallet.network.res;

import com.google.gson.annotations.SerializedName;

public class ResLcdSingleBonding {

    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public ResLcdBonding result;
}
