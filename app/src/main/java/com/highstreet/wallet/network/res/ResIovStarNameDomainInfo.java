package com.highstreet.wallet.network.res;

import com.google.gson.annotations.SerializedName;
import com.highstreet.wallet.model.StarNameDomain;

public class ResIovStarNameDomainInfo {
    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public IovStarnameDomainInfoValue result;

    @SerializedName("error")
    public String error;


    public class IovStarnameDomainInfoValue {
        @SerializedName("domain")
        public StarNameDomain domain;

    }
}
