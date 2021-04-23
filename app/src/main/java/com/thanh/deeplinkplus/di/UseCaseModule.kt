package com.thanh.deeplinkplus.di

import com.thanh.deeplinkplus.repo.update.UpdateRepo
import com.thanh.deeplinkplus.storage.AppPreferences
import com.thanh.deeplinkplus.storage.local_db.database.AppDatabase
import com.thanh.deeplinkplus.usecase.UrlUseCase
import com.thanh.deeplinkplus.usecase.UrlUseCaseImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val useCaseModule = Kodein.Module("USECASE_MODULE", false) {
    bind<UrlUseCase>() with provider {
        createUrlUseCase(instance(), instance(), instance())
    }
}

fun createUrlUseCase(preferences: AppPreferences, database: AppDatabase, updateRepo: UpdateRepo): UrlUseCase = UrlUseCaseImpl(preferences, database, updateRepo)