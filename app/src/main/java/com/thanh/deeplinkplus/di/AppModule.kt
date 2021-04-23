package com.thanh.deeplinkplus.di

import androidx.databinding.DataBindingUtil.bind
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.thanh.deeplinkplus.common.resources.Resources
import com.thanh.deeplinkplus.config.Configs
import com.thanh.deeplinkplus.network.RequestInterceptor
import com.thanh.deeplinkplus.storage.AppPreferences
import com.thanh.deeplinkplus.storage.local_db.database.AppDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = Kodein.Module("APP_MODULE", false) {


    bind<RequestInterceptor>() with singleton {
        requestInterceptor()
    }

    bind<HttpLoggingInterceptor>() with singleton {
        loggingInterceptor()
    }

    bind() from  singleton {
        okHttpClient(instance(), instance())
    }

    bind() from singleton {
        retrofit(instance())
    }
    bind() from singleton {
        createAppResources()
    }

    bind() from singleton {
        createAppPreferences()
    }

    bind() from singleton {
        createAppDatabase()
    }
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