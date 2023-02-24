package com.iye03.currencyexchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.iye03.currencyexchange.api.ExchangeService
import com.iye03.currencyexchange.api.model.ExchangeRates
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private var buyUsdTextView: TextView? = null
    private var sellUsdTextView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buyUsdTextView = findViewById(R.id.txtBuyUsdRate)
        sellUsdTextView = findViewById(R.id.txtSellUsdRate)

        ExchangeService.exchangeApi().getExchangeRates().enqueue(object :
            Callback<ExchangeRates> {
            override fun onResponse(call: Call<ExchangeRates>, response:
            Response<ExchangeRates>) {
                val responseBody: ExchangeRates? = response.body();
                buyUsdTextView?.text = responseBody?.lbpToUsd.toString()
                sellUsdTextView?.text = responseBody?.usdToLbp.toString()
            }
            override fun onFailure(call: Call<ExchangeRates>, t: Throwable) {
                return;
            }
        })


    }
}