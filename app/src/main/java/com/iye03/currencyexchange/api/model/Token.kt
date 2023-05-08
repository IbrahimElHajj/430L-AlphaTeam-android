package com.iye03.currencyexchange.api.model

import com.google.gson.annotations.SerializedName
class Token {
    @SerializedName("token")
    var token: String? = null
    @SerializedName("role")
    var role: String? = null
}