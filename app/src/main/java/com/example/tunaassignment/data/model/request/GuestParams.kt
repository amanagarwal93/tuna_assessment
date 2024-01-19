package com.example.tunaassignment.data.model.request

import com.example.tunaassignment.utils.Constants

data class GuestParams(
    val device_id: String,
    val device_type: String = "2",
    val push_token: String = "",
    val secret_key: String = Constants.API_KEY
)
