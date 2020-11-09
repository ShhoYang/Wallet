package com.highstreet.wallet.network.res;

import com.google.gson.annotations.SerializedName;
import com.highstreet.wallet.model.type.Redelegate;

import java.util.ArrayList;

public class ResLcdRedelegate {

    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public ArrayList<Redelegate> result;

}
