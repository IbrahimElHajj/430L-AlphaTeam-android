package com.iye03.currencyexchange.api.model

import com.google.gson.annotations.SerializedName
import java.util.Date

class TraceList() {
    @SerializedName("averagesLbpToUsd")
    var averagesLbpToUsd: List<Float>? = null
    @SerializedName("averagesUsdToLbp")
    var averagesUsdToLbp: List<Float>? = null
    @SerializedName("dates")
    var dates: List<String>? = null

    //input
    @SerializedName("timeFormat")
    var timeFormat: String? = null
}