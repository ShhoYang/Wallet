package com.highstreet.wallet.network.res;

import com.google.gson.annotations.SerializedName;
import com.highstreet.wallet.dao.OkToken;

import java.util.ArrayList;

public class ResOkTokenList {
    @SerializedName("code")
    public int code;

    @SerializedName("msg")
    public String msg;

    @SerializedName("detail_msg")
    public String detail_msg;

    @SerializedName("data")
    public ArrayList<OkToken> data;
}
