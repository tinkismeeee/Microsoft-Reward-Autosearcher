package com.tinkismee.microsort_reward_autosearcher

import retrofit2.Call
import retrofit2.http.*

interface API_getChromeVersion {
    @GET("last-known-good-versions.json")
    fun getChromeVersion(
        @Header("Content-Type") contentType: String
    ) : Call<ChromeVersionResponse>
}