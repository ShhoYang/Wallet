package com.highstreet.wallet.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;
import com.highstreet.wallet.model.type.Fee;
import com.highstreet.wallet.model.type.Msg;
import com.highstreet.wallet.model.type.Signature;

import java.util.ArrayList;

public class StdTx {

    @SerializedName("type")
    public String type;

    @SerializedName("value")
    public Value value;

    public static class Value {

        @SerializedName("msg")
        public ArrayList<Msg> msg;

        @SerializedName("fee")
        public Fee fee;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @SerializedName("signatures")
        public ArrayList<Signature> signatures;

        @SerializedName("memo")
        public String memo;
    }
}
