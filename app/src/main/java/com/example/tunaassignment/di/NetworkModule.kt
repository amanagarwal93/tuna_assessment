package com.example.tunaassignment.di

import androidx.viewbinding.BuildConfig
import com.example.tunaassignment.utils.Constants
import com.example.tunaassignment.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        when {
            BuildConfig.DEBUG -> interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        return interceptor
    }

    @Provides
    fun provideOkHttpClient(
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(Constants.TIMEOUT.toLong(), TimeUnit.SECONDS)
            .connectTimeout(Constants.TIMEOUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(provideHTTPLoggingInterceptor())
            .build()
    }

    // create a retrofit instance
    @Provides
    fun getInstance(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}

