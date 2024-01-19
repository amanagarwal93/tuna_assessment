package com.example.tunaassignment.data.repository

import com.example.tunaassignment.data.model.request.EscapeRoomParams
import com.example.tunaassignment.data.model.request.GuestParams
import com.example.tunaassignment.data.model.request.LoginParams
import com.example.tunaassignment.data.model.request.MovieParams
import com.example.tunaassignment.data.model.response.EscapeRoomResponse
import com.example.tunaassignment.data.model.response.LoginResponse
import com.example.tunaassignment.data.model.response.MovieResponse
import com.example.tunaassignment.data.model.response.TokenResponse
import com.example.tunaassignment.data.remote.CinergyApi
import com.example.tunaassignment.utils.Error
import com.example.tunaassignment.utils.Result
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val retrofit: Retrofit) {

    private var api: CinergyApi = retrofit.create(CinergyApi::class.java)

    //fetch Token
    suspend fun getToken(deviceId: String): Result<TokenResponse> {

        return getResponse(
            request = { api.getGuestToken(GuestParams(deviceId)) },
            defaultErrorMessage = "Error fetching token"
        )
    }

    // fetch User
    suspend fun getUser(token: String, loginParams: LoginParams): Result<LoginResponse> {

        return getResponse(
            request = { api.getUser("Bearer $token", loginParams) },
            defaultErrorMessage = "Error fetching user details"
        )
    }

    // fetch EscapeRooms
    suspend fun getEscapeRooms(token: String, userId: String, escapeRoomParams: EscapeRoomParams):
            Result<EscapeRoomResponse> {

        return getResponse(
            request = { api.getEscapeRoomsList("Bearer $token", userId, escapeRoomParams) },
            defaultErrorMessage = "Error fetching escape rooms"
        )
    }

    // fetch Movie Info
    suspend fun getMovieInfo(token: String, userId: String, movieParams: MovieParams):
            Result<MovieResponse> {

        return getResponse(
            request = { api.getMovieInfo("Bearer $token", userId, movieParams) },
            defaultErrorMessage = "Error fetching movie info"
        )
    }

    // get response
    private suspend fun <T> getResponse(
        request: suspend () -> Response<T>,
        defaultErrorMessage: String
    ): Result<T> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful) {
                return Result.success(result.body())
            } else {
                val errorResponse = parseError(result, retrofit)
                Result.error(
                    errorResponse?.status_message ?: defaultErrorMessage, errorResponse
                )
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            Result.error("Unknown Error", null)
        }
    }

    private fun parseError(response: Response<*>, retrofit: Retrofit): Error? {
        val converter = retrofit.responseBodyConverter<Error>(Error::class.java, arrayOfNulls(0))
        return try {
            converter.convert(response.errorBody()!!)
        } catch (e: IOException) {
            Error()
        }
    }
}

