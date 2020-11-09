package com.highstreet.wallet.network.res;

import com.google.gson.annotations.SerializedName;
import com.highstreet.wallet.model.type.BnbHistory;

import java.util.ArrayList;

public class ResBnbHistories {

    @SerializedName("tx")
    public ArrayList<BnbHistory> tx;

}
