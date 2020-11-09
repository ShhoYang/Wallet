package com.highstreet.wallet.network.res;

import com.google.gson.annotations.SerializedName;
import com.highstreet.wallet.model.type.Proposal;

public class ResLcdProposal {

    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public Proposal result;
}
