package com.example.tunaassignment.ui.escapeRoom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tunaassignment.data.model.request.EscapeRoomParams
import com.example.tunaassignment.data.model.request.MovieParams
import com.example.tunaassignment.data.model.response.EscapeRoomResponse
import com.example.tunaassignment.data.model.response.MovieResponse
import com.example.tunaassignment.data.repository.RemoteRepository
import com.example.tunaassignment.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EscapeRoomViewModel @Inject constructor(private val repository: RemoteRepository) :
    ViewModel() {

    private val escapeRoomResponse = MutableLiveData<Result<EscapeRoomResponse>>()
    val getEscapeRoom = escapeRoomResponse

    private val movieResponse = MutableLiveData<Result<MovieResponse>>()
    val getMovieResponse = movieResponse

    // fetch token from repository
    fun fetchEscapeRoom(token: String, userId: String, escapeRoomParams: EscapeRoomParams) {
        viewModelScope.launch {
            repository.fetchEscapeRooms(token, userId, escapeRoomParams).collect {
                escapeRoomResponse.value = it
            }
        }
    }

    // fetch token from repository
    fun fetchMovieInfo(token: String, userId: String, movieParams: MovieParams) {
        viewModelScope.launch {
            repository.fetchMovieInfo(token, userId, movieParams).collect {
                movieResponse.value = it
            }
        }
    }
}
