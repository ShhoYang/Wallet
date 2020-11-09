package com.highstreet.wallet.network;

import com.highstreet.wallet.network.res.ResCgcTic;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CoinGeckoService {

    @GET("api/v3/coins/{chain}")
    Call<ResCgcTic> getPriceTic(@Path("chain") String chain);
}
