package com.highstreet.wallet.network.res;

import com.google.gson.annotations.SerializedName;
import com.highstreet.wallet.model.StarNameAccount;

import java.util.ArrayList;

public class ResIovStarNameAccount {
    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public IovAccountValue result;

    public class IovAccountValue {
        @SerializedName("accounts")
        public ArrayList<StarNameAccount> accounts;

    }
}
