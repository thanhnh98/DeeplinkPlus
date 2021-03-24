package com.thanh.deeplinkplus.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

abstract class BaseActivity: AppCompatActivity() {

    private lateinit var baseViewModel: BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::baseViewModel.isInitialized)
            baseViewModel.onDestroy()
    }

    internal fun initViewModel(viewModel: BaseViewModel){
        baseViewModel = ViewModelProvider(this).get(viewModel::class.java)
        baseViewModel.init()
    }
}