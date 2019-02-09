package com.joe.donlate.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Place(
    @SerializedName("status") @Expose val status: String,
    @SerializedName("addresses") @Expose val addresses: List<Address>
)

data class Address(
    @SerializedName("roadAddress") @Expose val road: String,
    @SerializedName("jibunAddress") @Expose val jibun: String,
    @SerializedName("x") @Expose val lon: String,
    @SerializedName("y") @Expose val lat: String,
    @SerializedName("errorMessage") @Expose val errorMessage: String
)