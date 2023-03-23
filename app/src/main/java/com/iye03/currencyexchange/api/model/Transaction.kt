package com.iye03.currencyexchange.api.model

import com.google.gson.annotations.SerializedName
import java.util.Date

class Transaction() {
    @SerializedName("usd_amount")
    var usdAmount: Float? = null
    @SerializedName("lbp_amount")
    var lbpAmount: Float? = null
    @SerializedName("usd_to_lbp")
    var usdToLbp: Boolean? = null
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("added_date")
    var addedDate: String? = null
}