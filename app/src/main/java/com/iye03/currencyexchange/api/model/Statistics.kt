package com.iye03.currencyexchange.api.model

import com.google.gson.annotations.SerializedName

class Statistics {
    @SerializedName("lbpRateChangeBasedOnTimeFormatBetweenDates")
    var lbpRateChangeBasedOnTimeFormatBetweenDates: Float? = null

    @SerializedName("usdRateChangeBasedOnTimeFormatBetweenDates")
    var usdRateChangeBasedOnTimeFormatBetweenDates: Float? = null

    @SerializedName("numberOfLbpTransactionsBetweenDates")
    var numberOfLbpTransactionsBetweenDates: Int? = null

    @SerializedName("numberOfTransactionsBetweenDates")
    var numberOfTransactionsBetweenDates: Int? = null

    @SerializedName("numberOfUsdTransactionsBetweenDates")
    var numberOfUsdTransactionsBetweenDates: Int? = null

    @SerializedName("totalTransactionsToday")
    var totalTransactionsToday: Int? = null
}