package com.example.tunaassignment.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tunaassignment.data.model.request.LoginParams
import com.example.tunaassignment.data.model.response.LoginResponse
import com.example.tunaassignment.data.model.response.TokenResponse
import com.example.tunaassignment.data.repository.RemoteRepository
import com.example.tunaassignment.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: RemoteRepository) : ViewModel() {

    private val tokenResponse = MutableLiveData<Result<TokenResponse>>()
    val getToken = tokenResponse

    private val loginResponse = MutableLiveData<Result<LoginResponse>>()
    val getLogin = loginResponse

    // fetch token from repository
    fun fetchToken(deviceId: String) {
        viewModelScope.launch {
            repository.fetchToken(deviceId).collect { tokenResponse.value = it }
        }
    }

    // fetch token from repository
    fun fetchUserLogin(token: String, loginParams: LoginParams) {
        viewModelScope.launch {
            repository.fetchUser(token, loginParams).collect { loginResponse.value = it }
        }
    }
}
