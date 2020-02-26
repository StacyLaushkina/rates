package ru.laushkina.rates.network;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class RatesResponse {
    @SerializedName("source")
    public String Source;

    @SerializedName("quotes")
    public Map<String, Float> Rates;
}
