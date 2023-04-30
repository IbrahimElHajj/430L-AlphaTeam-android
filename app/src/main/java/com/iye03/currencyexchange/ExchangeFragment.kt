package com.iye03.currencyexchange

import android.app.Application
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.iye03.currencyexchange.api.ExchangeService
import com.iye03.currencyexchange.api.model.ExchangeRates
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class ExchangeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    private var buyUsdTextView: TextView? = null
    private var sellUsdTextView: TextView? = null

    private var lbpBuyNum: EditText? = null
    private var usdBuyNum: EditText? = null
    private var lbpSellNum: EditText? = null
    private var usdSellNum: EditText? = null
    private var lock: Boolean = false

    private fun fetchRates(){
        ExchangeService.exchangeApi(this.requireActivity().application).getExchangeRates().enqueue(object :
            Callback<ExchangeRates> {

            override fun onResponse(call: Call<ExchangeRates>, response:
            Response<ExchangeRates>
            ) {
                val responseBody: ExchangeRates? = response.body();
                buyUsdTextView?.text = responseBody?.lbpToUsd.toString()
                buyUsdTextView?.postInvalidate()
                sellUsdTextView?.text = responseBody?.usdToLbp.toString()
                sellUsdTextView?.postInvalidate()
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

    fun listener(view: TextView, i:Int, k: KeyEvent): Boolean {
        return false
    }

    fun setUpChangeHandlers(){
        lbpBuyNum?.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!lock) {
                    lock = true
                    var lbp = try {lbpBuyNum?.text.toString().toDouble()} catch (e: NumberFormatException){0.0}
                    try {
                        var rate = buyUsdTextView?.text.toString().toDouble()
                        usdBuyNum?.setText(
                            ((lbp / rate * 100).roundToInt().toDouble() / 100).toString()
                        )
                    }
                    catch(e:java.lang.Exception){
                        lock = false
                    }
                }else{
                    lock = false
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        usdBuyNum?.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!lock) {
                    lock = true
                    var usd = try{usdBuyNum?.text.toString().toDouble()} catch (e: NumberFormatException){0.0}
                    try{
                        var rate = buyUsdTextView?.text.toString().toDouble()
                        lbpBuyNum?.setText(
                            ((usd * rate * 100).roundToLong().toDouble()/100).toString()
                        )
                    }
                    catch(e:java.lang.Exception){
                        lock = false
                    }
                }else{
                    lock = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        lbpSellNum?.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!lock) {
                    lock = true
                    var lbp = try {lbpSellNum?.text.toString().toDouble()} catch (e: NumberFormatException){0.0}
                    try{
                        var rate = sellUsdTextView?.text.toString().toDouble()
                        usdSellNum?.setText(
                            ((lbp / rate * 100).roundToInt().toDouble() / 100).toString()
                        )
                    }
                    catch(e:java.lang.Exception){
                        lock = false
                    }
                }else{
                    lock = false
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        usdSellNum?.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!lock) {
                    lock = true
                    var usd = try{usdSellNum?.text.toString().toDouble()} catch (e: NumberFormatException){0.0}
                    try{
                        var rate = sellUsdTextView?.text.toString().toDouble()
                        lbpSellNum?.setText(
                            ((usd * rate * 100).roundToLong().toDouble()/100).toString()
                        )
                    }
                    catch(e:java.lang.Exception){
                        lock = false
                    }
                }else{
                    lock = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_exchange, container, false)

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshExchange);
        swipeRefreshLayout?.setOnRefreshListener(this);

        buyUsdTextView = view.findViewById(R.id.txtBuyUsdRate)
        sellUsdTextView = view.findViewById(R.id.txtSellUsdRate)

        lbpBuyNum = view.findViewById(R.id.lbpbuy)
        usdBuyNum = view.findViewById(R.id.usdbuy)
        lbpSellNum = view.findViewById(R.id.lbpsell)
        usdSellNum = view.findViewById(R.id.usdsell)

        setUpChangeHandlers()

        return view
    }

    override fun onRefresh() {
        fetchRates()
        swipeRefreshLayout?.setRefreshing(false)

    }

}