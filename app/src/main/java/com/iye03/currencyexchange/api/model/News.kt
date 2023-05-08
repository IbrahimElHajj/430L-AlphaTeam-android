package com.iye03.currencyexchange.api.model

import com.google.gson.annotations.SerializedName

class News {
    @SerializedName("news")
    var news: String? = null
    @SerializedName("newscaster_username")
    var newscaster_username: String? = null
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("added_date")
    var addedDate: String? = null
}