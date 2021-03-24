package com.thanh.deeplinkplus.di

import android.content.Context
import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.thanh.deeplinkplus.app.DeepLinkApplication
import com.thanh.deeplinkplus.common.resources.Resources
import com.thanh.deeplinkplus.config.Configs
import com.thanh.deeplinkplus.network.RequestInterceptor
import com.thanh.deeplinkplus.repo.update.UpdateRepo
import com.thanh.deeplinkplus.storage.AppPreferences
import com.thanh.deeplinkplus.storage.local_db.database.AppDatabase
import com.thanh.deeplinkplus.usecase.UrlUseCase
import com.thanh.deeplinkplus.usecase.UrlUseCaseImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {

    single { loggingInterceptor() }
    single { requestInterceptor() }
    single { okHttpClient(get(), get()) }
    single { retrofit(get()) }
    single { createAppResources() }
    single { createAppPreferences() }
    single { createAppDatabase() }
}

fun requestInterceptor() = RequestInterceptor()

fun loggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

fun okHttpClient(requestInterceptor: RequestInterceptor, loggingInterceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
    .readTimeout(Configs.SERVER_TIMEOUT, TimeUnit.SECONDS)
    .connectTimeout(Configs.SERVER_TIMEOUT, TimeUnit.SECONDS)
    .addInterceptor(requestInterceptor)
    .addInterceptor(loggingInterceptor)
    .build()

fun retrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
    .baseUrl(Configs.HOST)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

fun createAppResources() = Resources.getResources()

fun createAppPreferences() = AppPreferences.getInstance()

fun createAppDatabase() = AppDatabase.getInstance()