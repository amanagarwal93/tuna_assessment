package com.example.tunaassignment.data.model.response

data class LoginResponse(
    val attractions_menu: Int,
    val food_menu: Int,
    val response: String,
    val splash_images: SplashImages,
    val user: User
)

data class SplashImages(
    val splash_image_bottom: String,
    val splash_image_top: String
)

data class User(
    val access_token: Any,
    val address1: Any,
    val address2: Any,
    val birthday: Any,
    val booking_token: Any,
    val cinema_id: Any,
    val city: Any,
    val created_at: String,
    val deleted_at: Any,
    val email: Any,
    val encrypted_password: Any,
    val fb_login: Any,
    val first_name: Any,
    val gender: Any,
    val gmail_login: Any,
    val id: Int,
    val image: Any,
    val ip_access: Any,
    val is_active: Int,
    val last_name: Any,
    val location_id: Any,
    val location_name: String,
    val member_id: Int,
    val member_level_name: String,
    val name: String,
    val opt_out1: Int,
    val opt_out2: Int,
    val phone: String,
    val phone_code: String,
    val role_id: Int,
    val subscribed_member: Int,
    val updated_at: String,
    val user_role: Int,
    val username: String,
    val zip: Any
)

