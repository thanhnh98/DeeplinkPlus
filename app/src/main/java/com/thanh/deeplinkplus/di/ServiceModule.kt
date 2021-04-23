package com.thanh.deeplinkplus.di

import com.thanh.deeplinkplus.repo.Service
import com.thanh.deeplinkplus.repo.update.UpdateRepo
import com.thanh.deeplinkplus.repo.update.UpdateRepoImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

val serviceModule = Kodein.Module("SERVICE_MODULE", false) {
    bind() from singleton {
        createService<Service>(instance())
    }
    bind() from singleton {
        createUpdateService(instance())
    }
}

inline fun <reified T> createService(retrofit: Retrofit): T {
    return retrofit.create(T::class.java)
}

fun createUpdateService(service: Service): UpdateRepo = UpdateRepoImpl(service)