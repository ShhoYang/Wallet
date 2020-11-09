package com.highstreet.wallet.network.res;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResLcdBondings {

    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public ArrayList<ResLcdBonding> result;

}
