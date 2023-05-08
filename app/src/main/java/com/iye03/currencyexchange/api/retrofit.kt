package com.iye03.currencyexchange.api

import android.app.Application
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import android.content.Context;
import android.os.Trace
import com.iye03.currencyexchange.api.model.*


object ExchangeService {
    private const val API_URL: String = "http://alpha-exchange-rate.herokuapp.com"
    fun exchangeApi(application:Application):Exchange {
        val cacheSize = (5 * 1024 * 1024).toLong() // 5 MB
        val cache = Cache(application.cacheDir, cacheSize)
        val client = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor { chain ->
                var request = chain.request()
                if (request.method() == "GET") {
                    request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build()
                }
                chain.proceed(request)
            }
            .build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        // Create an instance of our Exchange API interface.
        return retrofit.create(Exchange::class.java);
    }
    interface Exchange {
        @GET("/exchangeRate")
        fun getExchangeRates(): Call<ExchangeRates>
        @POST("/transaction")
        fun addTransaction(@Body transaction: Transaction,
        @Header("Authorization") authorization:String?): Call<Any>

        @GET("/transaction")
        fun getTransactions(@Header("Authorization") authorization: String):
                Call<List<Transaction>>

        @POST("/statistics")
        fun getStatistics(@Body traceData:TraceList): Call<Statistics>

        @POST("/user")
        fun addUser(@Body user:User): Call<User>

        @POST("/authentication")
        fun authenticate(@Body user:User): Call<Token>

        @POST("/transaction/datapoints")
        fun getTraceData(@Body traceData:TraceList): Call<TraceList>

        @GET("/news")
        fun getNews(): Call<List<News>>
        @POST("/news/post")
        fun postNews(@Body news: News,@Header("Authorization") authorization: String?): Call<News>

        @POST("/usertransaction")
        fun addTransactionP2P(@Body transaction: Transaction,
                           @Header("Authorization") authorization:String?): Call<Any>

        @GET("/usertransaction/list/offers")
        fun getTransactionsP2P(): Call<List<Transaction>>

        @GET("/usertransaction/list/user")
        fun getMyTransactionsP2P(@Header("Authorization") authorization: String?): Call<List<Transaction>>
    }
}
