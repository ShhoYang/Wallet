package com.highstreet.wallet.network.res;

import com.google.gson.annotations.SerializedName;

public class ResLcdSingleUnBonding {

    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public ResLcdUnBonding result;
}
