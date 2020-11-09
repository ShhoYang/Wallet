package com.highstreet.wallet.network.res;

import com.google.gson.annotations.SerializedName;
import com.highstreet.wallet.model.type.Tally;

public class ResLcdProposalTally {
    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public Tally result;

}
