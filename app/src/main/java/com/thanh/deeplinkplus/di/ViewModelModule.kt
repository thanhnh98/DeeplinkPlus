package com.thanh.deeplinkplus.di

import com.thanh.deeplinkplus.screen.home.viewmodel.HomeViewModel
import com.thanh.deeplinkplus.usecase.UrlUseCase
import com.thanh.deeplinkplus.usecase.UrlUseCaseImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { createHomeViewModel(get()) }
}

fun createHomeViewModel(urlUseCase: UrlUseCase) = HomeViewModel(urlUseCase = urlUseCase)