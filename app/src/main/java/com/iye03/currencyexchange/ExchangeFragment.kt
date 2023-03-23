package com.iye03.currencyexchange

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.iye03.currencyexchange.api.ExchangeService
import com.iye03.currencyexchange.api.model.ExchangeRates
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExchangeFragment : Fragment() {
    private var buyUsdTextView: TextView? = null
    private var sellUsdTextView: TextView? = null

    private fun fetchRates(){
        ExchangeService.exchangeApi().getExchangeRates().enqueue(object :
            Callback<ExchangeRates> {

            override fun onResponse(call: Call<ExchangeRates>, response:
            Response<ExchangeRates>
            ) {
                val responseBody: ExchangeRates? = response.body();
                buyUsdTextView?.text = responseBody?.lbpToUsd.toString()
                sellUsdTextView?.text = responseBody?.usdToLbp.toString()
            }

            override fun onFailure(call: Call<ExchangeRates>, t: Throwable) {
                return;
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchRates()
    }

    override fun onResume() {
        super.onResume()
        fetchRates()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_exchange, container, false)

        buyUsdTextView = view.findViewById(R.id.txtBuyUsdRate)
        sellUsdTextView = view.findViewById(R.id.txtSellUsdRate)

        return view
    }
}