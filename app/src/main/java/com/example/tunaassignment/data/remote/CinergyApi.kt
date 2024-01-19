package com.example.tunaassignment.data.remote

import com.example.tunaassignment.data.model.request.EscapeRoomParams
import com.example.tunaassignment.data.model.request.GuestParams
import com.example.tunaassignment.data.model.request.LoginParams
import com.example.tunaassignment.data.model.request.MovieParams
import com.example.tunaassignment.data.model.response.EscapeRoomResponse
import com.example.tunaassignment.data.model.response.LoginResponse
import com.example.tunaassignment.data.model.response.MovieResponse
import com.example.tunaassignment.data.model.response.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface CinergyApi {

    @POST("guestToken")
    suspend fun getGuestToken(
        @Body guestParams: GuestParams
    ): Response<TokenResponse>

    @POST("login")
    suspend fun getUser(
        @Header("Authorization") token: String,
        @Body loginParams: LoginParams
    ): Response<LoginResponse>

    @POST("escapeRoomMovies")
    suspend fun getEscapeRoomsList(
        @Header("Authorization") token: String,
        @Header("userId") userId: String,
        @Body escapeRoomParams: EscapeRoomParams
    ): Response<EscapeRoomResponse>

    @POST("getMovieInfo")
    suspend fun getMovieInfo(
        @Header("Authorization") token: String,
        @Header("userId") userId: String,
        @Body movieParams: MovieParams
    ): Response<MovieResponse>
}
