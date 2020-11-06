package com.thanh.deeplinkplus.screen.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.thanh.deeplinkplus.R
import com.thanh.deeplinkplus.extension.fadeIn
import com.thanh.deeplinkplus.screen.home.HomeActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.util.concurrent.TimeUnit


class SplashScreen: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        tv_app_name?.apply {
            fadeIn(500)
                .andThen(Observable.timer(500, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete {
                        navigateToMain()
                    })
                .subscribe()
        }

    }

    private fun navigateToMain(){
        val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, tv_app_name, "tv_app_name")
        startActivity(Intent(this@SplashScreen, HomeActivity::class.java))
        this.finish()

    }
}