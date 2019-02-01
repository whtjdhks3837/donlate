package com.joe.donlate.api

import com.joe.donlate.data.Place
import com.joe.donlate.util.NAVER_MAP_SEARCH_CLIENT_ID
import com.joe.donlate.util.NAVER_MAP_SEARCH_CLIENT_SECRET
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface NaverMapService {
    @GET("map-geocode/v2/geocode")
    @Headers(
        "X-NCP-APIGW-API-KEY-ID: $NAVER_MAP_SEARCH_CLIENT_ID",
        "X-NCP-APIGW-API-KEY: $NAVER_MAP_SEARCH_CLIENT_SECRET"
    )
    fun getAddress(
        @Query("query") query: String,
        @Query("count") count: Int
    ): Single<Place>
}