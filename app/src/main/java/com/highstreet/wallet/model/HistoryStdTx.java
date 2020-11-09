package com.highstreet.wallet.model;

import com.google.gson.annotations.SerializedName;
import com.highstreet.wallet.model.type.Fee;
import com.highstreet.wallet.model.type.Msg;

import java.util.ArrayList;

public class HistoryStdTx {

    @SerializedName("type")
    public String type;

    @SerializedName("value")
    public HistoryStdTx.Value value;

    public static class Value {

        @SerializedName("msg")
        public ArrayList<Msg> msg;

        @SerializedName("fee")
        public Fee fee;

        @SerializedName("memo")
        public String memo;
    }
}
