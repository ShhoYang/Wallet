package com.highstreet.wallet.network.res;

import com.google.gson.annotations.SerializedName;
import com.highstreet.wallet.model.type.Coin;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ResKavaSwapSupply2 {

    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public ArrayList<KavaSwapSupply2> result;


    public class KavaSwapSupply2{
        @SerializedName("incoming_supply")
        public Coin incoming_supply;

        @SerializedName("outgoing_supply")
        public Coin outgoing_supply;

        @SerializedName("current_supply")
        public Coin current_supply;

        @SerializedName("time_limited_current_supply")
        public Coin time_limited_current_supply;

        @SerializedName("time_elapsed")
        public String time_elapsed;

    }


    public KavaSwapSupply2 getSwapSupply(String denom) {
        if (result != null && result.size() > 0) {
            for (KavaSwapSupply2 supply:result) {
                if (denom.toLowerCase().startsWith(supply.incoming_supply.denom.toLowerCase())) {
                    return supply;
                }
            }
        }
        return null;
    }

    public BigDecimal getRemainCap(String denom, BigDecimal supplyLimit) {
        BigDecimal remain = BigDecimal.ZERO;
        try {
            KavaSwapSupply2 supply = getSwapSupply(denom);
            BigDecimal incoming_supply = new BigDecimal(supply.incoming_supply.amount);
            BigDecimal current_supply = new BigDecimal(supply.current_supply.amount);
            remain = supplyLimit.subtract(current_supply).subtract(incoming_supply);

        }catch (Exception e) {}
        return remain;
    }

}
