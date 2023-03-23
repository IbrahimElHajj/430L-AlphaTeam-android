package com.iye03.currencyexchange.api

import com.iye03.currencyexchange.api.model.ExchangeRates
import com.iye03.currencyexchange.api.model.Token
import com.iye03.currencyexchange.api.model.Transaction
import com.iye03.currencyexchange.api.model.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


object ExchangeService {
    private const val API_URL: String = "http://10.0.2.2:5000"
    fun exchangeApi():Exchange {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        // Create an instance of our Exchange API interface.
        return retrofit.create(Exchange::class.java);
    }
    interface Exchange {
        @GET("/exchangeRate")
        fun getExchangeRates(): Call<ExchangeRates>
        @POST("/transaction")
        fun addTransaction(@Body transaction: Transaction): Call<Any>

        @POST("/user")
        fun addUser(@Body user:User): Call<User>

        @POST("/signin")
        fun authenticate(@Body user:User): Call<Token>
    }
}