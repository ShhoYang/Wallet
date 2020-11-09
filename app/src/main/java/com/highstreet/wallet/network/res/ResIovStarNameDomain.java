package com.highstreet.wallet.network.res;

import com.google.gson.annotations.SerializedName;
import com.highstreet.wallet.model.StarNameDomain;

import java.util.ArrayList;

public class ResIovStarNameDomain {
    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public IovDomainValue result;


    public class IovDomainValue {
        @SerializedName("Domains")
        public  ArrayList<StarNameDomain> Domains;

    }
}
