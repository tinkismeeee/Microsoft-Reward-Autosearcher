package com.tinkismee.microsort_reward_autosearcher

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class localQueryDataClass(
    val title: String,
    val queries: List<String>
)
