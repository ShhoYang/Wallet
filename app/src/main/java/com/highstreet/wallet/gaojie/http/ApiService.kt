package com.highstreet.wallet.gaojie.http

import com.highstreet.wallet.base.BaseChain
import com.highstreet.wallet.gaojie.AccountManager
import com.socks.library.KLog
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */
object ApiService {

    private const val BASE_URL_MAIN = "https://rpc.dippernetwork.com/"
    private const val BASE_URL_TEST = "https://rpc.testnet.dippernetwork.com/"

    private var dipApi: DipApi? = null
    fun getDipApi(): DipApi {
        if (dipApi == null) {
            synchronized(ApiService::class.java) {

                val url = if (BaseChain.DIP_TEST.chain == AccountManager.instance().chain.chain) {
                    BASE_URL_TEST
                } else {
                    BASE_URL_MAIN
                }
                val retrofit = Retrofit.Builder()
                        .baseUrl(url)
                        .client(getOkHttp())
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()
                dipApi = retrofit.create(DipApi::class.java)
            }
        }
        return dipApi!!
    }

    private fun getOkHttp(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                KLog.json("json__", message)
            }

        })
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
                .addNetworkInterceptor(NetInterceptor())
                .retryOnConnectionFailure(true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .build()
    }
}

class NetInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
                .addHeader("Connection", "close")
                .addHeader("accept", "application/json")
                .method(original.method(), original.body())
                .build()
        return chain.proceed(request)
    }
}