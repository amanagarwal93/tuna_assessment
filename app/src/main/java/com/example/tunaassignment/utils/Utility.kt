package com.example.tunaassignment.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

// show view
fun View.show() {
    visibility = View.VISIBLE
}

// hide view
fun View.hide() {
    visibility = View.GONE
}

/**
 * Show snack bar by passing message and duration
 */
fun View.snack(message: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, message, duration).show()
}
