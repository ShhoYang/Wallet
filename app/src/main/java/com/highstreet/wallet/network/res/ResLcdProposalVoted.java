package com.highstreet.wallet.network.res;

import com.google.gson.annotations.SerializedName;
import com.highstreet.wallet.model.type.Vote;

import java.util.ArrayList;

public class ResLcdProposalVoted {

    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public ArrayList<Vote> result;
}
