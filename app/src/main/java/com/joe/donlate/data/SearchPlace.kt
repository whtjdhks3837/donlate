package com.joe.donlate.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Place(
    @SerializedName("status") @Expose val status: String,
    @SerializedName("addresses") @Expose val places: List<Address>
)

data class Address(
    @SerializedName("roadAddress") @Expose val roadAddress: String,
    @SerializedName("jibunAddress") @Expose val jibunAddress: String,
    @SerializedName("x") @Expose val lon: String,
    @SerializedName("y") @Expose val lat: String,
    @SerializedName("errorMessage") @Expose val errorMessage: String
)