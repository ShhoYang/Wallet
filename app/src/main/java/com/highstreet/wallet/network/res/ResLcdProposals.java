package com.highstreet.wallet.network.res;

import com.google.gson.annotations.SerializedName;
import com.highstreet.wallet.model.type.Proposal;

import java.util.ArrayList;

public class ResLcdProposals {
    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public ArrayList<Proposal> result;
}
