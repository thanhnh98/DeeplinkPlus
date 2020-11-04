package com.thanh.deeplinkplus.screen.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.thanh.deeplinkplus.R
import com.thanh.deeplinkplus.extension.fadeIn
import com.thanh.deeplinkplus.screen.home.HomeActivity
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.CompletableSubject
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.util.concurrent.TimeUnit

class SplashScreen: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        tv_app_name?.apply {
            fadeIn(500)
                .andThen(Observable.timer(2000, TimeUnit.MICROSECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete {
                        startActivity(Intent(this@SplashScreen, HomeActivity::class.java))
                        this@SplashScreen.finish()
                    })
                .subscribe()
        }

    }
}