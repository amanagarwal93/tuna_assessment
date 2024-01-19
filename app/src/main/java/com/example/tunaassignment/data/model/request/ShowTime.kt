package com.example.tunaassignment.data.model.request

import android.os.Parcelable
import com.example.tunaassignment.data.model.response.Session
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShowTime(
    var date: String = "",
    var sessions: List<Session> = emptyList()
) : Parcelable

