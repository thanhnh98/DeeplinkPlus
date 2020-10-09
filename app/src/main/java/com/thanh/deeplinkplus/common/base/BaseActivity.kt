package com.thanh.deeplinkplus.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<T:IBasePresenter>: AppCompatActivity() {

    protected var mPresenter: T = getPresenter()
    protected abstract fun getPresenter(): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.onViewCreated()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }
}