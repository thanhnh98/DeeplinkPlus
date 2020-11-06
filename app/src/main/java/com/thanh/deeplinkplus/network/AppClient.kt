package com.thanh.deeplinkplus.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AppClient {
    companion object{
        private var mRetrofit: Retrofit? = null
        private var mInterceptor: RequestInterceptor? = null
        private const val TIMEOUT_TIME: Long = 60

        private val HOST:String? = "https://github.com/thanhnh98/DeeplinkPlus/blob/master/app/src/main/assets/"
        @JvmStatic
        fun init(){
            val log: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = BODY
            }

            mInterceptor = RequestInterceptor()

            var okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
                .readTimeout(TIMEOUT_TIME, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT_TIME, TimeUnit.SECONDS)
                .addInterceptor(mInterceptor)
                .addInterceptor(log)

            mRetrofit = Retrofit.Builder()
                .baseUrl(HOST)
                .client(okHttpClientBuilder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }

        fun <T>createService(serviceClass: Class<T>): T{
            if (mRetrofit == null)
                throw IllegalStateException("Must call init() before")

            val requestRetrofit = mRetrofit

            return requestRetrofit?.create(serviceClass)?:throw NullPointerException("requestRetrofit is NULL")
        }
    }
}