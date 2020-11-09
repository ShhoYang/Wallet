package com.highstreet.wallet.network;

import com.highstreet.wallet.network.req.ReqMoonPayKey;
import com.highstreet.wallet.network.req.ReqPushAlarm;
import com.highstreet.wallet.network.res.ResMoonPaySignature;
import com.highstreet.wallet.network.res.ResPushAlarm;
import com.highstreet.wallet.network.res.ResVersionCheck;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Cosmostation {

    @GET("/v1/app/version/android")
    Call<ResVersionCheck> getVersion();

    @POST("/v1/account/update")
    Call<ResPushAlarm> updateAlarm(@Body ReqPushAlarm data);

    @POST("/v1/sign/moonpay")
    Call<ResMoonPaySignature> getMoonPay(@Body ReqMoonPayKey data);
}
