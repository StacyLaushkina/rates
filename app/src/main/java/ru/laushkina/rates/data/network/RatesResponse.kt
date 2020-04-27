package ru.laushkina.rates.data.network

import com.google.gson.annotations.SerializedName

class RatesResponse {
    @SerializedName("source")
    lateinit var Source: String

    @SerializedName("quotes")
    lateinit var Rates: Map<String, Float>
}