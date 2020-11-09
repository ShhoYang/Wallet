package com.highstreet.wallet.network.res;

import com.google.gson.annotations.SerializedName;
import com.highstreet.wallet.model.StarNameAccount;

import java.util.ArrayList;

public class ResIovStarNameAccountInDomain {

    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public IovAccountInDomainValue result;

    public class IovAccountInDomainValue {
        @SerializedName("accounts")
        public ArrayList<StarNameAccount> accounts;

    }
}
