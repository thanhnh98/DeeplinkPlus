package com.thanh.deeplinkplus.common.base

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<T>: AppCompatActivity() {

    protected var mPresenter: T = getPresenter()

    protected abstract fun getPresenter(): T
}