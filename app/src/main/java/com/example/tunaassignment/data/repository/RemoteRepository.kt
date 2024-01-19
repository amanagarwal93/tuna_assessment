package com.example.tunaassignment.data.repository

import com.example.tunaassignment.data.model.request.EscapeRoomParams
import com.example.tunaassignment.data.model.request.LoginParams
import com.example.tunaassignment.data.model.request.MovieParams
import com.example.tunaassignment.data.model.response.EscapeRoomResponse
import com.example.tunaassignment.data.model.response.LoginResponse
import com.example.tunaassignment.data.model.response.MovieResponse
import com.example.tunaassignment.data.model.response.TokenResponse
import com.example.tunaassignment.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) {

    // fetch token from remote
    suspend fun fetchToken(deviceId: String): Flow<Result<TokenResponse>> {
        return flow {
            emit(Result.loading())
            emit(remoteDataSource.getToken(deviceId))
        }.flowOn(Dispatchers.IO)
    }

    // fetch user from remote
    suspend fun fetchUser(token: String, loginParams: LoginParams): Flow<Result<LoginResponse>> {
        return flow {
            emit(Result.loading())
            emit(remoteDataSource.getUser(token, loginParams))
        }.flowOn(Dispatchers.IO)
    }

    // fetch escape rooms from remote
    suspend fun fetchEscapeRooms(token: String, userId: String, escapeRoomParams: EscapeRoomParams)
            : Flow<Result<EscapeRoomResponse>> {
        return flow {
            emit(Result.loading())
            emit(remoteDataSource.getEscapeRooms(token, userId, escapeRoomParams))
        }.flowOn(Dispatchers.IO)
    }

    // fetch movie info  from remote
    suspend fun fetchMovieInfo(token: String, userId: String, movieParams: MovieParams)
            : Flow<Result<MovieResponse>> {
        return flow {
            emit(Result.loading())
            emit(remoteDataSource.getMovieInfo(token, userId, movieParams))
        }.flowOn(Dispatchers.IO)
    }
}
