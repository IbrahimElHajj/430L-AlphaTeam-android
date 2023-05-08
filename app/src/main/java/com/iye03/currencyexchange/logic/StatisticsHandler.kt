package com.iye03.currencyexchange.logic

import android.app.Application
import android.view.View
import android.widget.TextView
import com.iye03.currencyexchange.R
import com.iye03.currencyexchange.api.ExchangeService
import com.iye03.currencyexchange.api.model.ExchangeRates
import com.iye03.currencyexchange.api.model.Statistics
import com.iye03.currencyexchange.api.model.TraceList
import lecho.lib.hellocharts.model.AxisValue
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.math.roundToLong

class StatisticsHandler(private val view:View, private val application: Application) {
    private final var totalToday: TextView? = null
    private final var totalWeekBuy: TextView? = null
    private final var totalWeekSell: TextView? = null
    private final var totalWeek: TextView? = null
    private final var deltaBuy: TextView? = null
    private final var deltaSell: TextView? = null

    init{
        totalToday = view.findViewById(R.id.txtTodayT)
        totalWeekBuy = view.findViewById(R.id.txtTWeek)
        totalWeekSell = view.findViewById(R.id.txtBuyWeek)
        totalWeek = view.findViewById(R.id.txtSellWeek)
        deltaBuy = view.findViewById(R.id.txtLastWeekDeltaBuy)
        deltaSell = view.findViewById(R.id.txtLastWeekDeltaSell)
    }

    fun refresh(spinnerValue: Any?){
        var format = "Hourly"
        if(spinnerValue == "1 Day"){
            format = "Hourly"
        }
        else if(spinnerValue == "1 Week"){
            format = "Daily"
        }
        else if(spinnerValue == "1 Month"){
            format = "Weekly"
        }
        val traceData: TraceList = TraceList()
        traceData.timeFormat = format
        traceData.startDate = Instant.now().epochSecond
        traceData.endDate = StatisticsHelper.getStartFromFormat(format)

        ExchangeService.exchangeApi(application).getStatistics(traceData).enqueue(object :
            Callback<Statistics> {

            override fun onResponse(call: Call<Statistics>, response:
            Response<Statistics>
            ) {
                val responseBody: Statistics? = response.body();
                totalToday?.text = responseBody?.totalTransactionsToday.toString()
                totalToday?.postInvalidate()
                totalWeekBuy?.text = responseBody?.numberOfLbpTransactionsBetweenDates.toString()
                totalWeekBuy?.postInvalidate()
                totalWeekSell?.text = responseBody?.numberOfUsdTransactionsBetweenDates.toString()
                totalWeekSell?.postInvalidate()
                totalWeek?.text = responseBody?.numberOfTransactionsBetweenDates.toString()
                totalWeek?.postInvalidate()
                responseBody?.lbpRateChangeBasedOnTimeFormatBetweenDates?.let{
                    deltaBuy?.text = ((it*100).roundToLong().toDouble()/100).toString()
                    deltaBuy?.postInvalidate()
                }
                responseBody?.usdRateChangeBasedOnTimeFormatBetweenDates?.let {
                    deltaSell?.text = ((it*100).roundToLong().toDouble()/100).toString()
                    deltaSell?.postInvalidate()
                }

            }

            override fun onFailure(call: Call<Statistics>, t: Throwable) {
                return;
            }

        })
    }
}

object StatisticsHelper {
    public fun getStartFromFormat(format: String) :Long?{
        val cal = Calendar.getInstance()
        cal.time = Date.from(Instant.now())
        if(format == "Hourly") {
            cal.add(Calendar.DAY_OF_YEAR, -1)
            return cal.time.toInstant().epochSecond
        }
        if(format == "Daily") {
            cal.add(Calendar.WEEK_OF_YEAR, -1)
            return cal.time.toInstant().epochSecond
        }
        if(format == "Weekly") {
            cal.add(Calendar.MONTH, -1)
            return cal.time.toInstant().epochSecond
        }
        return null
    }
}