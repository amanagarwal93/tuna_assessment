package com.example.tunaassignment.data.model.response

data class TokenResponse(
    val attractions_menu: Int,
    val food_menu: Int,
    val id: Int,
    val location: Any,
    val printer_name: Any,
    val response: String,
    val token: String
)
