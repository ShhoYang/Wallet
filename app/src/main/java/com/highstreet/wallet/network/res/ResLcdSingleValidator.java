package com.highstreet.wallet.network.res;

import com.google.gson.annotations.SerializedName;
import com.highstreet.wallet.model.type.Validator;

public class ResLcdSingleValidator {

    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public Validator result;
}
