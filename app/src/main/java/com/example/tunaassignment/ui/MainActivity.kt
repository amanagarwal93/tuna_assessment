package com.example.tunaassignment.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tunaassignment.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        when {
            supportActionBar != null -> supportActionBar!!.hide()
        }
        setContentView(activityMainBinding.root)
    }
}
