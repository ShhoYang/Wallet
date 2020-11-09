package com.highstreet.wallet.network.res;

import com.google.gson.annotations.SerializedName;
import com.highstreet.wallet.model.type.Vote;

public class ResMyVote {

    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public Vote result;
}
