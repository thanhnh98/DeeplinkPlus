package com.thanh.deeplinkplus.di

import com.thanh.deeplinkplus.repo.Service
import com.thanh.deeplinkplus.repo.update.UpdateRepo
import com.thanh.deeplinkplus.repo.update.UpdateRepoImpl
import org.koin.dsl.module
import retrofit2.Retrofit

val serviceModule = module {
    single { createService<Service>(get()) }
    single { createUpdateService(get()) }
}

inline fun <reified T> createService(retrofit: Retrofit): T {
    return retrofit.create(T::class.java)
}

fun createUpdateService(service: Service): UpdateRepo = UpdateRepoImpl(service)