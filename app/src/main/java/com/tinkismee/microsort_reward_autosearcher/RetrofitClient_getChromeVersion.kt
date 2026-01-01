package com.tinkismee.microsort_reward_autosearcher

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

object RetrofitClient_getChromeVersion {
    private const val BASE_URL = "https://googlechromelabs.github.io/chrome-for-testing/"

    val instance: API_getChromeVersion by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(API_getChromeVersion::class.java)
    }
}