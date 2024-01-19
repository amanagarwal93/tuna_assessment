package com.example.tunaassignment.data.model.response

data class EscapeRoomResponse(
    val er_tickets: String,
    val escape_rooms_movies: List<EscapeRoomsMovy>,
    val response: String
)

data class EscapeRoomsMovy(
    val ID: String,
    val Rating: String,
    val RunTime: String,
    val ScheduledFilmId: String,
    val Synopsis: String,
    val Title: String,
    val image_url: String,
    val image_url_poster: String,
    val slug: String
)
