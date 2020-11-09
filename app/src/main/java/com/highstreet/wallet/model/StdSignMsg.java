package com.highstreet.wallet.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.highstreet.wallet.model.type.Fee;
import com.highstreet.wallet.model.type.Msg;

import java.nio.charset.Charset;
import java.util.ArrayList;

public class StdSignMsg {

    @SerializedName("chain_id")
    public String chain_id;

    @SerializedName("account_number")
    public String account_number;

    @SerializedName("sequence")
    public String sequence;

    @SerializedName("fee")
    public Fee fee;

    @SerializedName("msgs")
    public ArrayList<Msg> msgs;

    @SerializedName("memo")
    public String memo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public byte[] getToSignByte() {
        Gson Presenter = new GsonBuilder().disableHtmlEscaping().create();
        return Presenter.toJson(this).getBytes(Charset.forName("UTF-8"));
    }
}
