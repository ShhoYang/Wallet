package com.highstreet.wallet.network.res;

import com.google.gson.annotations.SerializedName;
import com.highstreet.wallet.model.type.Coin;

import java.util.ArrayList;

public class ResKavaIncentiveParam {

    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public IncentiveParam result;


    public class IncentiveParam {

        @SerializedName("active")
        public Boolean active;

        @SerializedName("rewards")
        public ArrayList<IncentiveReward> rewards;

        public boolean isActive() {
            if (active && rewards != null && rewards.size() > 0 ) {
                return true;
            } else {
                return false;
            }
        }

    }

    public class IncentiveReward {

        @SerializedName("active")
        public Boolean active;

        @SerializedName("denom")
        public String denom;

        @SerializedName("available_rewards")
        public Coin available_rewards;

        @SerializedName("duration")
        public String duration;

        @SerializedName("time_lock")
        public String time_lock;

        @SerializedName("claim_duration")
        public String claim_duration;
    }


}
