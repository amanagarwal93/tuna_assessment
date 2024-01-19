package com.example.tunaassignment.data.model.request

data class EscapeRoomParams(
    val device_id: String,
    val device_type: String,
    val location_id: String,
    val login_type: String,
    val member_id: String
)
