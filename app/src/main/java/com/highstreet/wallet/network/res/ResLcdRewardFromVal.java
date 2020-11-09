package com.highstreet.wallet.network.res;

import com.google.gson.annotations.SerializedName;
import com.highstreet.wallet.model.type.Coin;

import java.util.ArrayList;

public class ResLcdRewardFromVal {

    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public ArrayList<Coin> result;
}
