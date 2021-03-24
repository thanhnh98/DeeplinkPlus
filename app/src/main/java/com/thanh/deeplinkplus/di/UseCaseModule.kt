package com.thanh.deeplinkplus.di

import com.thanh.deeplinkplus.repo.update.UpdateRepo
import com.thanh.deeplinkplus.storage.AppPreferences
import com.thanh.deeplinkplus.storage.local_db.database.AppDatabase
import com.thanh.deeplinkplus.usecase.UrlUseCase
import com.thanh.deeplinkplus.usecase.UrlUseCaseImpl
import org.koin.dsl.module

val useCaseModule = module {
    single { createUrlUseCase(get(), get(), get()) }
}

fun createUrlUseCase(preferences: AppPreferences, database: AppDatabase, updateRepo: UpdateRepo): UrlUseCase = UrlUseCaseImpl(preferences, database, updateRepo)